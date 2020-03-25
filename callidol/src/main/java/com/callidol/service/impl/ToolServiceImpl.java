package com.callidol.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.callidol.common.CIResult;
import com.callidol.common.UserInCache;
import com.callidol.common.UserResult;
import com.callidol.service.ToolService;


// /tool/**

@Service
public class ToolServiceImpl implements ToolService{
    
	@Autowired
	private UserInCache userInCache;
	
	@Override
	public CIResult addUserRestCallChance(int num, long userId) {
	    int rest = userInCache.addRestCallChance(userId, num);
	    UserResult userResult = new UserResult();
	    userResult.setRestChance(rest);
	    return CIResult.ok("OK", userResult);
	}
    
}
