package com.callidol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
    	return callService.getUserRestChance(user.getId().toString());
    }
	
	@RequestMapping("/getCallChance")
    public CIResult getCallRestChance() {
		
		User user = ValidatorInterceptor.getUser();
    	return callService.getCallRestChance(user.getId().toString());
    }
	
}
