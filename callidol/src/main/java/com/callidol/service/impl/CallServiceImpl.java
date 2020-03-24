package com.callidol.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.callidol.common.CIResult;
import com.callidol.common.UserResult;
import com.callidol.service.CallService;
import com.callidol.utils.RedisOp;


@Service
public class CallServiceImpl implements CallService{
    
	@Autowired
	private RedisOp redisOp;
	
	@Override
	public CIResult getUserRestChance(String userId) {

		//当用户的restChance存在于redis中，直接得到用户的restChance
		//当用户的restChance不存在于redis中，直接将用户的restChance赋值为0
		long restChance = redisOp.hDecrement("RestCallChance", userId, 0);
		//redisOp.hGet(key, field)  当用户的restChance不存在于redis中，返回为null

		UserResult userResult = new UserResult();
		userResult.setRestChance((int)restChance);
		
		return CIResult.ok("ok", userResult);//只需要返回restChance  “callChance”: 24
	}

	@Override
	public CIResult getCallRestChance(String userId) {
        Date date = new Date();
        long currentTime = date.getTime();  
        
		//时间的判断
		//当用户的getCallRestChanceTime存在于redis中，直接得到用户的getCallRestChanceTime
		//当用户的getCallRestChanceTime不存在于redis中，直接将用户的getCallRestChanceTime赋值为0
		long getCallRestChanceTime = redisOp.hIncrement("RestCallChance-getCallRestChanceTime", userId, 0);
		
		if(currentTime - getCallRestChanceTime > 6 * 60 * 60 * 1000) {
			long restChance = redisOp.hIncrement("RestCallChance", userId, 5);
			UserResult userResult = new UserResult();
			userResult.setRestChance((int)restChance);
						
			//修改上次获取的时间
			redisOp.hSet("RestCallChance-getCallRestChanceTime", userId, currentTime);
			return  CIResult.ok("免费获取打榜次数成功", userResult);
		}else {
			long needTime = 6 * 60  - (currentTime - getCallRestChanceTime)/(60 * 1000);
			return  CIResult.ok("免费获取打榜次数失败，还没有到6个小时", "还需要"+needTime+"分钟 到达6个小时");
		}
	}

}
