package com.callidol.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import com.callidol.common.RankAndScore;


/*
 * 对redis数据库的操作
 * */


@Component
public class RedisOp {
	public final static String GetRankAndScoreScript = 
			  "local rank = redis.call('ZREVRANK', KEYS[1], KEYS[2]);"//ZREVRANK 查询排名 key1:KEYS[1]  key2:KEYS[2]
			+ "local res = {};"//返回给java的字典
			+ "if rank == false then "//排名不存在
			    + "local rank = redis.call('ZCARD', KEYS[1]) + 1; "//ZCARD 查询这个列表的大小。返回这个列表的总数
			    + "res['score']=0.0; "
			    + "res['rank'] = rank; "
			    + "return cjson.encode(res);"//将字典转化为json字符串
			+ "else "
			+     "local score = redis.call('ZSCORE', KEYS[1], KEYS[2]);"//ZSCORE 求分数，求出来的是字符串
			+     "res['score']=tonumber(score); "
			+     "res['rank'] = rank + 1; "
			+     "return cjson.encode(res);"
			+ "end;";//将字典转化为json字符串
	
	/*
	 *  redis性质上和mysql一样，都是用来存储数据的，mongo，kafka，nsq，es，不同的数据库有各自的优势和使用场景
	 *  
	 *  mysql ->关系型数据，结构化数据 表示实体之间的关系，部分磁盘部分内存，并发性数据库，为了保证安全会经常加锁影响速度
	 *  
	 *  redis ->纯内存数据库，主要做缓存，单线程  curd ， ccuurrddddccr，没有并发问题，为啥redis比mysql快 
	 *   redis处理请求可以达到2w/s    mysql处理请求可以达到2000/s
	 *  :1. 纯内存
	 *   2. 数据简单 1）存储的key-value，不像mysql中的表有那么多字段  2）主要通过hash() 来查询，而不是像mysql遍历整张表来查询
	 *   3. 数据量小 虽然有很多人的session,但是只查询一个人的seesion,查询的数据量很小
	 *   4. 单线程
	 *  
	 * */
	
	//redis支持的数据类型和如何操作
	
	//reids -> map   Map<String, Object>
	
	// Object -> String, Int, Float, Map<String, (int, float, string)>, List(int, float, string), Set, Zset
	
	//Set (1, ,2 ,3)
	//List [1, 2 , 3,3, 3]  redis中的List 只能存放 String, Int, Float
	
	//ZSet[(name:value),(name:value),(name:value),(name:value)]
	
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
    
    
    public String get(String key) {
        
    	String val =  stringRedisTemplate.opsForValue().get(key);
    	
    	// get key
//    	if (val.equals("null"))
//    		return null;
    	return val;
    }
    
    public void set(String key, String value) {
    	stringRedisTemplate.opsForValue().set(key, value);
    }
    
    public boolean removeKey(String key) {
    	return stringRedisTemplate.delete(key);
    }

    
    public boolean hasKey(String key) {
		return stringRedisTemplate.hasKey(key);
	}
    
    public void set(String key, String value, int ttl) {
    	//如果ttl小于0则不设置过期时间
    	if(ttl < 0)
    		stringRedisTemplate.opsForValue().set(key, value);
    	else
    		stringRedisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
    }
    
    
    public long ttl(String key) { //ttl == time to live
    	return stringRedisTemplate.getExpire(key);
    }
    
    // --------------------hash----------
    public Object hGet(String key, String field) {
    	return stringRedisTemplate.opsForHash().get(key, field);
    }
    
    public void hSet(String key, Object field, Object value) {
    	stringRedisTemplate.opsForHash().put(key, field.toString(), value.toString());
    }
    
//    “restChance”: Map<>;
//    Map key:id, value: restChance;
    
    public long hIncrement(String key, String field, long delta) {
    	return stringRedisTemplate.opsForHash().increment(key, field, delta);
    }
    
    public long hDecrement(String key, Object field, long delta) {
    	return stringRedisTemplate.opsForHash().increment(key, field.toString(), -delta);
    }
    
    
    //- -----lua ------------
   
    //---------------zset------
    //增加zset中某个成员的分数
    public double zincr(String zsetName, Object member, double delta) {
    	return stringRedisTemplate.opsForZSet().incrementScore(zsetName, member.toString(), delta);
    }
    
    //获取zset中某个成员的分数和排名
    public double zscoreAndRank(String zsetName, Object member) {
    	return stringRedisTemplate.opsForZSet().score(zsetName, member.toString());//接受了多少次打榜
    }
    
    //同时zset中同时获取分数和排名
    public RankAndScore getRankAndScore(String zsetName, Object member) {	
	    DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(GetRankAndScoreScript);
        redisScript.setResultType(String.class);
        List<String> keys = new ArrayList<>();
 
        keys.add(zsetName);
        keys.add(member.toString());
      
        String rankAndScore = stringRedisTemplate.execute(redisScript, keys);
      
        return JsonUtil.jsonToPojo(rankAndScore, RankAndScore.class);
    }
}
