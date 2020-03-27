package com.callidol.testredis;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.callidol.common.CallInCache;
import com.callidol.common.RankAndScore;
import com.callidol.utils.RedisOp;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestZSet {
    
	@Autowired
    private StringRedisTemplate redisTemplate;
	
	@Autowired
	private RedisOp redisOp;
	
	
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
		
		
//		keys.add("ZEEEET");
//        keys.add("ttt");
        
//		RankAndScore rankAndScore = redisOp.getRankAndScore("ZEEEET", "wdw");
//		System.out.println(rankAndScore.getRank());
//		System.out.println(rankAndScore.getScore());
		
		List<RankAndScore> rankAndScoreList = redisOp.reverseRangeWithScores("12rafaf", 0, 20);
		System.out.println(rankAndScoreList.size());
	}
	
	
}
