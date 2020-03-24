package com.callidol.testredis;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestHash {
	@Autowired
    private StringRedisTemplate redisTemplate;
	
	@Test
	public void testHash() {
		System.out.println(redisTemplate.opsForHash().get("testmap", "name"));//wdw
		
		// redisTemplate.opsForHash().put("testmap", "name", "wdw");
		
		Map<String, Object> user = new HashMap<>();
		
		user.put("name", "wdw");
		user.put("age", "23");
		user.put("tall", "18");
		
	    redisTemplate.opsForHash().putAll("testmap", user);
	    
	    System.out.println(redisTemplate.opsForHash().get("testmap", "age")); //23
	    System.out.println(redisTemplate.opsForHash().increment("testmap", "age", 1)); //24
	    
	    System.out.println(redisTemplate.opsForHash().size("testmap")); //3
	    
	    Set keys = redisTemplate.opsForHash().keys("testmap");//
	    
	    for(Object key: keys)
	    	System.out.println(key);//name tall age
	}
}
