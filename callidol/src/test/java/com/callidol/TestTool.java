package com.callidol;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.callidol.utils.HashUtil;
import com.callidol.utils.SessionUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTool {
    	
	@Autowired
	private SessionUtil session;
	
	
	@Test
	public void deleteRegisterUserFromSession() {
		String code = HashUtil.hash("245369629@qq.com" + "callidol123456789");
		System.out.println(session.removeActivationRegisterUser(code));
	}
}
