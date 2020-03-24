package com.callidol.testredis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestFloat {
    
	@Autowired
    private StringRedisTemplate redisTemplate;
	
	@Test
	public void testFloat() {
		
		// System.out.println(redisTemplate.opsForValue().increment(key, delta));
		// 2.34
		
	}
}
