package com.callidol.utils;

import java.util.Calendar;

public class DateUtil {
	
	private Calendar cal = Calendar.getInstance();
    
	public long getTimeMillis() {
		return cal.getTimeInMillis();
	}
	
	public DateUtil(long time) {
		// https://blog.csdn.net/yhj19920417/article/details/73799842
		cal.setTimeInMillis(time);
	}
	
	public DateUtil() {}
	
    public String getYear() {
    	return String.format("%d", cal.get(Calendar.YEAR));
    }
    
    public String getMonth() {
    	return String.format("%d%d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
    }
    
    public String getWeek() {
    	return String.format("%d%d", cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR));
    }
    
    public static void main(String[] args) {
		DateUtil dUtil = new DateUtil();
		System.out.println(dUtil.getMonth());
		System.out.println(dUtil.getWeek());
		System.out.println(dUtil.getYear());
	}
}
