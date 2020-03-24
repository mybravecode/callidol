package com.callidol.utils;


import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


/*
 * 对redis数据库的操作
 * */


@Component
public class RedisOp {
	
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
    
    
    public Object hGet(String key, String field) {
    	return stringRedisTemplate.opsForHash().get(key, field);
    }
    
    public void hSet(String key, String field, Object value) {
    	stringRedisTemplate.opsForHash().put(key, field, value.toString());
    }
//    “restChance”: Map<>;
//    Map key:id, value: restChance;
    
    public long hIncrement(String key, String field, long delta) {
    	return stringRedisTemplate.opsForHash().increment(key, field, delta);
    }
    
    public long hDecrement(String key, String field, long delta) {
    	return stringRedisTemplate.opsForHash().increment(key, field, -delta);
    }
    
    
}
