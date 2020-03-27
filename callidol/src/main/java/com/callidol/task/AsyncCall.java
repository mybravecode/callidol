package com.callidol.task;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.callidol.common.CallInCache;
import com.callidol.common.CallMsg;
import com.callidol.service.CallService;
import com.callidol.utils.DateUtil;
import com.callidol.utils.JsonUtil;
import com.callidol.utils.RedisOp;

//spring boot整合定时任务


@Configuration
@EnableScheduling
public class AsyncCall {
		
	@Autowired
	private CallService callService;
	
	@Autowired
	private CallInCache callInCache;
	
	@Scheduled(fixedRate=20000)
    public void asyncCallForIdol() {
		//去redis中拿取打榜信息
		//根据信息异步完成增加明星和用户打榜数的任务
		
		while(true) {
			CallMsg callMsg = callInCache.getUserCallMsg(5, TimeUnit.SECONDS);
            
			if(callMsg == null)
				return ;
			
			long callTime = callMsg.getTime();
			
			DateUtil dateUtil = new DateUtil(callTime);
			
			//调用service里面的userCallForIdol
			callService.userCallForHisIdol(callMsg.getUserId(), callMsg.getIdolId(), callMsg.getCallNum(), dateUtil);
		}//while true
		
    }
}
