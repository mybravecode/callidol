package com.callidol;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestZsetRange {
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
    @Test
    public void testZsetRange() {
    	
    	//1 2 3 4 5
    	//range 0, 3 1234
    	//reverseRange
//    	redisTemplate.opsForZSet().
    	
    }
}
