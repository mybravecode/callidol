package com.callidol;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.callidol.utils.Mail;

//
//如果不用单元测试，必须在启动某个服务的情况下，测试某个使用了spring boot自动注入的对象的函数

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUnitTest {
	
	@Autowired private Mail mail;
	

	private void ttt() {
		System.out.println(1 / 0);
	}
	
	@Test
	public void testEqual() {
//		if(1 == 1) {
//			try {
//				throw new Exception();
//			}catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
		
		//ttt();
	}
	
	@Test
	public void testB() {
		System.out.println("this is a unit testing B!");
	}
	
	
	@Test
    public void testA() {
    	System.out.println("this is a unit testing A!");
    }
	
	
}
