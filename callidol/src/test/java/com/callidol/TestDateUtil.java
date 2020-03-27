package com.callidol;

import com.callidol.utils.DateUtil;

public class TestDateUtil {
    public static void main(String[] args) throws InterruptedException {
		DateUtil dateUtil = new DateUtil();
		long time = dateUtil.getTimeMillis();
		System.out.println(time);
		
		Thread.sleep(3003);
		
		DateUtil dateUtil2 = new DateUtil(time);
		
		System.out.println(dateUtil2.getTimeMillis());
	}
}
