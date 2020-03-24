package com.callidol.service;

import javax.servlet.http.HttpServletResponse;

import com.callidol.common.CIResult;
import com.callidol.pojo.User;

public interface UserService {
	
    public CIResult registerUser(String mail, String nickname, String password);
    
    public CIResult loginUser(String mail, String password, HttpServletResponse response);

    
    public CIResult activateUser(String code);
    
    public CIResult getUserLoginLink(String mail);

	public CIResult eloginUserByMail(String loginCode, HttpServletResponse response);
    
}
