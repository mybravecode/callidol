package com.callidol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.callidol.common.CIResult;
import com.callidol.interceptor.ValidatorInterceptor;
import com.callidol.service.IdolService;

@RestController  // = Controller + ResponseBody 返回json字符串
@RequestMapping("/apiv1/idol")
public class IdolController {
	
	@Autowired
	private IdolService idolService;
	
	//输入名字搜索返回明星信息
	@RequestMapping("/search")
	public CIResult searchIdolByName(@RequestParam String name) {
		if(name == null || name.equals("")) {
			return CIResult.error("请输入正确的明星姓名");
		}
		
		return idolService.searchIdolByName(name);
	}
	
	//根据明星id查询明星信息
	@RequestMapping("/info")
	public CIResult getIdolInfoById(@RequestParam Long idolId) {
		if(idolId == null || idolId < 0) {
			return CIResult.error("id < 0     请输入正确的id");
		}
		long userId = ValidatorInterceptor.getUser().getId();
		
		return idolService.getIdolInfoById(idolId, userId); //idolId, userId
	}


}
