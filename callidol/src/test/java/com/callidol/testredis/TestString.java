package com.callidol.testredis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestString {
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    
    
    @Test
    public void testGet() {
    	
    	System.out.println("testGet");
    	
    	System.out.println(redisTemplate.opsForValue().get("runoob"));
    	
    	redisTemplate.opsForValue().set("runoob", "菜鸟一只");
    	
    	System.out.println(redisTemplate.opsForValue().get("runoob"));
    	
    	redisTemplate.hasKey("runoob");
    	
    	redisTemplate.getExpire("runoob"); //
    	
    	redisTemplate.opsForValue().set("runoob", "一只菜鸟", 10);
    	
    }
    
    
}
