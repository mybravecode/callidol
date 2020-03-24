package com.callidol.testredis;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Clear {
    
	@Autowired
    private StringRedisTemplate redisTemplate;
	
	@Test
	public void clearRedis() {
		Set<String> keys = redisTemplate.keys("*");
		redisTemplate.delete(keys);
	}
	
	
}
