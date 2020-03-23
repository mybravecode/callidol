package com.callidol.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.callidol.common.CIResult;
import com.callidol.mapper.UserMapper;
import com.callidol.service.UserService;


@RestController  // = Controller + ResponseBody 返回json字符串
@RequestMapping("/apiv1/user")
public class UserController {
		
	@Autowired
	private UserService userService;
	
	//用户注册接口
	@RequestMapping("/register")
	public CIResult registerUser(@RequestParam String mail,
			                 @RequestParam String nickname,
			                 @RequestParam String password,
			                 @RequestParam String confirm) {
	    
		//基本检测
		// 1. 邮箱是否符合标准 qq,163.. TODO
		// 2.昵称密码是否为空
		// 3.密码的合法性 TODO
		// 4.两次密码是否一样
		
		if(nickname == "" || password == "") {
			return CIResult.error("昵称或者密码为空");
		}
		
		if(!password.equals(confirm)) {
			return CIResult.error("两次密码不一样");
		}
		
	    
		return userService.registerUser(mail, nickname, password);
		
	}
	
	
	//用户登录接口
	@RequestMapping("/login")
	public CIResult loginUser(@RequestParam String mail,
			                 @RequestParam String password,
			                 HttpServletResponse response) {
	    
        
		if(mail == null || password == null || mail.equals("") || password.equals(""))
			return CIResult.error("账号密码格式错误");
		
		
		return userService.loginUser(mail, password, response);
		
	}
	
	@RequestMapping("/activate")
	public CIResult activateUser(@RequestParam String code) {
		if(code == "" || code == null)
			return CIResult.error("无效激活码");
		
		return userService.activateUser(code);
	}
	
	
	@RequestMapping("/access")
	public CIResult getUserLoginLink(@RequestParam String mail) {
		
		//根据正则判断邮箱合法性 TODO
		if(mail == null || mail == "")
			return CIResult.error("无效邮箱");
		
		return userService.getUserLoginLink(mail);
	}
	
	
	@RequestMapping("/elogin")
	public CIResult eloginUserByMail(@RequestParam String code, HttpServletResponse response) {
		
		//根据正则判断邮箱合法性 TODO
		if(code == null || code == "")
			return CIResult.error("无效登陆链接 无效loginCode，请检查邮箱中的登录链接是否正确");
		
		return userService.eloginUserByMail(code, response);
	}
}
