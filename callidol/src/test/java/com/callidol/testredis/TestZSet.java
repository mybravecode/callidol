package com.callidol.testredis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.callidol.common.CallInCache;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestZSet {
    
	@Autowired
    private StringRedisTemplate redisTemplate;
	
	@Autowired
	private CallInCache callInCache;
	
	@Test
	public void testZSet() {
		//
//		String testkey = "test-zset";
//		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++==");
//		
//		System.out.println(redisTemplate.opsForZSet().rank(testkey, "wdw")); //2
//		
//		redisTemplate.opsForZSet().add(testkey, "wdw", 10);
//		System.out.println(redisTemplate.opsForZSet().rank(testkey, "wdw")); //2
//		System.out.println(redisTemplate.opsForZSet().score(testkey, "wdw")); //10
//		
//		redisTemplate.opsForZSet().add(testkey, "fzz", 12);
//		redisTemplate.opsForZSet().add(testkey, "ttt", 2);
//		redisTemplate.opsForZSet().add(testkey, "tty", 9); 
//		//ttt(2) tty(9) wdw(10) fzz(12)
//		
//		System.out.println(redisTemplate.opsForZSet().rank(testkey, "wdw")); //2
//		System.out.println(redisTemplate.opsForZSet().score(testkey, "wdw")); //10
//		
//		System.out.println(redisTemplate.opsForZSet().size(testkey));// 4
//        
//		redisTemplate.opsForZSet().incrementScore(testkey, "ttt", 100);
//		
//		System.out.println(redisTemplate.opsForZSet().rank(testkey, "ttt")); //3
//		System.out.println(redisTemplate.opsForZSet().score(testkey, "ttt")); //10
//		
//		System.out.println("++++++++++++++++++++++++test-zset-incr-null+++++++++++++++++++++++==");
//		
//		testkey = "test-zset-incr-null";
//		
//		redisTemplate.opsForZSet().incrementScore(testkey, "ttt", 100);
//		System.out.println(redisTemplate.opsForZSet().rank(testkey, "ttt")); //3
//		System.out.println(redisTemplate.opsForZSet().score(testkey, "ttt")); //10
//		
		
		System.out.println(callInCache.getIdolCallNumWeek(123L));
		
	}
	
	
}
