package com.callidol.testredis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class TestInt {
    //redis int相关操作
	
	@Autowired
    private StringRedisTemplate redisTemplate;

	
	@Test
	public void testInt() {
		
		System.out.println(redisTemplate.opsForValue().get("num"));
		
		//redis支持直接对整数进行加减，并且返回加减之后的结果，如果该int对应的key不存在，则默认为0，然后进行加减
		System.out.println(redisTemplate.opsForValue().increment("num", 10));
		System.out.println(redisTemplate.opsForValue().increment("num", 20));
		System.out.println(redisTemplate.opsForValue().get("num"));
        //
		
		System.out.println("------------------age--------------");
		
		//redis使用increment可以直接对value为string使用，只要这个string是个数字
		redisTemplate.opsForValue().set("age", "10");
		System.out.println(redisTemplate.opsForValue().increment("age", 20));
		
		//
		System.out.println("----------------name----------------");
		redisTemplate.opsForValue().set("name", "ww");
		System.out.println(redisTemplate.opsForValue().increment("name", 20));
		System.out.println(redisTemplate.opsForValue().get("name"));
		
		// redisTemplate.opsForValue().decrement(key, delta)
		
	}
}
