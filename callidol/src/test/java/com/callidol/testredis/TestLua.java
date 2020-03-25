package com.callidol.testredis;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLua {
	@Autowired
    private StringRedisTemplate redisTemplate;
	
	
	
	@Test
	public void testLua() {
		// redisTemplate.opsForValue().set("wdw", 12);
		
//		System.out.println(redisTemplate.opsForValue().get("t12"));//12
//		
//		String testLuaScript = "redis.call('set', KEYS[1], ARGV[1]); redis.call('set', KEYS[2], ARGV[2]);return 1";
//		
//		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
//        redisScript.setScriptText(testLuaScript);
//        redisScript.setResultType(Long.class);
//        List<String> keys = new ArrayList<>();
//        keys.add("t12");
//       
//        
//		long res = redisTemplate.execute(redisScript, keys, "4569");
//		
//		System.out.println(redisTemplate.opsForValue().get("t12"));//12
//		
//		String callForIdolLuaScript = "local restChance = redis.call('HINCRBY', KEYS[1], KEYS[2], 0);"
//				+ "local callNum = tonumber(ARGV[1]);"// tonumber : 将String类型转化为int
//				+ "if(restChance < callNum) then return -1;" //如果剩余次数不够，返回-1
//				+ "else return redis.call('HINCRBY', KEYS[1], KEYS[2], -callNum) ;end";//否则 返回减去callNum的执行结果
//		
//		redisTemplate.opsForHash().increment("MapKe1", "restChance", 5);
//		//String testLuaScript = "redis.call('set', KEYS[1], ARGV[1]); redis.call('set', KEYS[2], ARGV[2]);return 1";
////		
//		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
//        redisScript.setScriptText(callForIdolLuaScript);
//        redisScript.setResultType(Long.class);
//        List<String> keys = new ArrayList<>();
//        
//        keys.add("MapKe1");
//        keys.add("restChance");
//        
//        long res = redisTemplate.execute(redisScript, keys, "6");
//        
//        System.out.println(res);
		
		String callForIdolLuaScript = 
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
		
			
		DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(callForIdolLuaScript);
        redisScript.setResultType(String.class);
        List<String> keys = new ArrayList<>();
        
        redisTemplate.opsForZSet().add("ZEEEET", "wdw", 10.5);
        redisTemplate.opsForZSet().add("ZEEEET", "fzz", 10.8);
        redisTemplate.opsForZSet().add("ZEEEET", "txy", 10.9);
        
        keys.add("ZEEEET");
        keys.add("ttt");
        
        String res = redisTemplate.execute(redisScript, keys);
        
        System.out.println(res);
	}
}
