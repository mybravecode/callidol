package com.callidol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.callidol.common.CIResult;
import com.callidol.interceptor.ValidatorInterceptor;
import com.callidol.pojo.User;
import com.callidol.service.CallService;


@RestController
@RequestMapping("/apiv1/call")
public class CallController {
	
	@Autowired
	private CallService callService;
		
	@RequestMapping("/restCallChance")
    public CIResult getUserRestChance() {
		
		User user = ValidatorInterceptor.getUser();
    	return callService.getUserRestChance(user.getId());
    }
	
	// /us
	
	@RequestMapping("getFreeCallChance")
    public CIResult getFreeCallRestChance() {
		
		User user = ValidatorInterceptor.getUser();
    	return callService.getFreeCallRestChance(user.getId());	
    	
    }
	
	
	//获取某个用户的专属分享链接（用来增加用户的打榜次数）
		@RequestMapping("/shareToIncrCallChance")
		public CIResult shareUserToIncrCallChance() {
				
			User user = ValidatorInterceptor.getUser();	
//			return userService.shareUserToIncrCallChance(user.getId().toString());
			return callService.shareToIncrCallChance(user);
		}
		
		
		//获取某个用户的专属分享链接（用来增加用户的打榜次数）
		@RequestMapping("/clickShare")
		public CIResult clickUserToIncrCallChance(@RequestParam String code) {
			if(code == null || code == "")
				return CIResult.error("无效分享码,用户的专属分享链接有误");
			
			return callService.clickShare(code);
		}
		
		
		@RequestMapping("/call")
	    public CIResult callForIdol(@RequestParam Long idolId, @RequestParam Integer callNum) {
			if(idolId == null)
				return CIResult.error("明星id为空");
			if(callNum == null || callNum <=0)
				return CIResult.error("请输入合法的打榜次数，必须>0");
			
			User user = ValidatorInterceptor.getUser();
			
	    	return callService.callForIdol(idolId, callNum, user.getId());
	    }
}
