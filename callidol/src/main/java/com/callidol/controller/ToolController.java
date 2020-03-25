package com.callidol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.callidol.common.CIResult;
import com.callidol.interceptor.ValidatorInterceptor;
import com.callidol.service.ToolService;


//工具，供测试改变数据使用

@RestController
@RequestMapping("/apiv1/tool")
public class ToolController {
	
	@Autowired
	private ToolService toolService;
	
	//增加用户剩余打榜次数
    @RequestMapping("/user/getRestChance")
    public CIResult addUserRestCallChance() {
    	return toolService.addUserRestCallChance(10, ValidatorInterceptor.getUser().getId());
    }
}
