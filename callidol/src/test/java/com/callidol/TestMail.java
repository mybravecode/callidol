package com.callidol;

import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.callidol.utils.Mail;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMail {
    @Autowired private Mail mail;
    
    @Test
    public void testHtmlMail() {
    	try {
			mail.sendHtmlMail("245369629@qq.com", "无题", "相见时难别亦难");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
