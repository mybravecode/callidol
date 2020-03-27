package com.callidol;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.test.context.junit4.SpringRunner;

import com.callidol.common.RankAndScore;
import com.callidol.utils.RedisOp;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestZsetRange {
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private RedisOp redisOp;
	
	
    @Test
    public void testZsetRange() {
    	
    	String zsetName = "RangeZet";
    	
    	redisTemplate.opsForZSet().add(zsetName, "wdw", 10.9);
    	
    	redisTemplate.opsForZSet().add(zsetName, "fzz", 15.9);

    	redisTemplate.opsForZSet().add(zsetName, "ttt", 0.9);

    	redisTemplate.opsForZSet().add(zsetName, "xxx", 18.9);

    	redisTemplate.opsForZSet().add(zsetName, "tyyy", 23);

    	redisTemplate.opsForZSet().add(zsetName, "vvv", 20.9);

    	
    	//1 2 3 4 5
    	//range 0, 3 1234
    	//reverseRange
//    	Set<TypedTuple<String>> range = redisTemplate.opsForZSet().reverseRangeWithScores(zsetName, 0, -1);
//    	for(TypedTuple<String> tuple: range) {
//    		System.out.println(tuple.getScore()+"..."+(tuple.getValue()));
//    		//getScore() 分数               getValue() 对应的值
////    		23.0...tyyy
////    		20.9...vvv
////    		18.9...xxx
////    		15.9...fzz
////    		10.9...wdw
////    		0.9...ttt
//    	}
    	
    	List<RankAndScore> rankAndScores = redisOp.reverseRangeWithScores(zsetName, 1, 6);
    	
    	System.out.println("-----------------------------------");
    	for(RankAndScore rankAndScore: rankAndScores)
    		System.out.println(rankAndScore);
    }
    
}
