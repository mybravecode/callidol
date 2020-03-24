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
public class TestSet {
    //{1, 1}
	
	@Autowired
    private StringRedisTemplate redisTemplate;
	
	@Test
	public void testSet() {
		
		//"sset": {}
		
		//关于set可以进行哪些操作呢
		
		System.out.println("pop null: " + redisTemplate.opsForSet().pop("ffff"));
		//
		//redis并不需要创建set的命令，直接用add就会默认创建set并将数据加进去
		redisTemplate.opsForSet().add("sset", "a", "ab", "d", "cc");
		
		System.out.println(redisTemplate.opsForSet().pop("sset"));
		
		Set<String> ssSet= redisTemplate.opsForSet().members("sset");
		System.out.println(ssSet);
		
		
	}
	
}
