package com.callidol;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.callidol.utils.RedisOp;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {
    @Autowired
    private RedisOp redisOp;
    
    @Test
    public void testUnexistsKey() {
    	System.out.println("=============");
    	System.out.println(redisOp.get("afdf"));
    	System.out.println("=============");
    }
}
