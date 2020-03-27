package com.callidol.testredis;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.callidol.utils.RedisOp;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestList {

	@Autowired
    private StringRedisTemplate redisTemplate;
	
	@Autowired
	private RedisOp redisOp;
	
	
	@Test
	public void testList() {
//		System.out.println(redisTemplate.opsForList().leftPop("testpush"));
//		System.out.println(redisTemplate.opsForList().size("testpush"));
//		
//		System.out.println(redisTemplate.opsForList().leftPush("testpush", "12"));
//		
//		System.out.println(redisTemplate.opsForList().leftPush("testpush", "12"));
//		
//		System.out.println(redisTemplate.opsForList().leftPush("testpush", "ab"));
//		//ab 12 12
//		System.out.println(redisTemplate.opsForList().leftPop("testpush")); //ab
//		
//		System.out.println(redisTemplate.opsForList().size("testpush")); //2
		
		String listName = "LLLISt";
		redisOp.lPush(listName, "123");
		redisOp.lPush(listName, "456");
		redisOp.lPush(listName, "789");
		redisOp.lPush(listName, "101");
		System.out.println(redisOp.rPopBlocking(listName, 5, TimeUnit.SECONDS));
		System.out.println(redisOp.rPopBlocking(listName, 5, TimeUnit.SECONDS));
		System.out.println(redisOp.rPopBlocking(listName, 5, TimeUnit.SECONDS));
		System.out.println(redisOp.rPopBlocking(listName, 5, TimeUnit.SECONDS));
		
		System.out.println(redisOp.rPopBlocking(listName, 5, TimeUnit.SECONDS));
	}
}
