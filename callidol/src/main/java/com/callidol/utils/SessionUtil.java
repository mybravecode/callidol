package com.callidol.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.callidol.pojo.User;

/*
 * session工具，主要用来在redis中存放session数据，或者取session数据
 * 一个session就是sessionId: sessionValue的形式，供后端进行用户的身份验证 
 * */


@Component
public class SessionUtil {
    @Autowired private RedisOp redis;
    
    // ----------------------------------模块：激活待注册用户---------------

    //模块：激活待注册用户 
    //传入 哈希且加盐的code  ,进行拼接操作
    public static String getActivationRegisterUserId(String code) {
    	return "activationCode-" + code + "-info";
    }
    
    //模块：激活待注册用户 
    //传入 哈希且加盐的code(未拼接)  ,判断redis中是否 包含该键
    public boolean hasActivationRegisterUser(String code) {  	
    	return redis.hasKey(getActivationRegisterUserId(code));
    }
    
    //模块：激活待注册用户 
    //将注册激活信息放入redis
    public void setActivationRegisterUser(String code, User user, int ttl) {
    	String userJson = JsonUtil.objectToJson(user);	
    	redis.set(getActivationRegisterUserId(code), userJson, ttl);
    }
    
    //模块：激活待注册用户 
    //根据activationCode从redis中取出用户
    public User getActivationRegisterUser(String activationCode) {
    	String userJson = redis.get(getActivationRegisterUserId(activationCode));
    	//redis里存了3个值么？redis.set(sessionId, userJson, ttl); 通过redis.get(sessionId)得到的是什么？userJson？
    	
    	if(userJson == null)
    		return null;
    	
    	return JsonUtil.jsonToPojo(userJson, User.class);
    }
    
    //模块：激活待注册用户 
    //删除redis中activationCode对应的 用户待激活信息
    public boolean removeActivationRegisterUser(String activationCode) {
        return redis.removeKey(getActivationRegisterUserId(activationCode));
    }
    
    
    
    
    
    // ----------------------------------通过点击邮箱中的链接 登录账号---------------
    //模块：点击邮箱的链接，实现不用密码的登录
    //传入 哈希且加盐的code  ,进行拼接操作
    public static String getUserLoginUrlInfoId(String code) {
    	return "login-url" + code + "-info";
    }
    
    //删除redis中存储的“点击邮箱的链接，实现不用密码的登录”的信息
    public boolean removeUserLoginUrlInfo(String loginCode) {
        return redis.removeKey(getUserLoginUrlInfoId(loginCode));
    }
    
    
    //将用户 code 和 mail 放入redis
    public void setUserLoginUrlInfo(String code, String mail, int ttl) {	
    	
    	redis.set(getUserLoginUrlInfoId(code), mail, ttl);
    }
    
    //根据loginCode从redis中取出用户邮箱
    public String getUserLoginUrlInfo(String loginCode) {
    	String mail = redis.get(getUserLoginUrlInfoId(loginCode));	
    	return mail;
    }
    
    
    //根据loginCode判断 是否已经生成对应的链接、链接是否在有效期内
    public boolean hasUserLoginUrlInfo(String loginCode) {
    	return redis.hasKey(getUserLoginUrlInfoId(loginCode));    	
    }
    
    // ----------------------------------获取某个用户的专属分享链接（用来增加用户的打榜次数)---------------
    public static String getShareUserToIncrCallChanceInfoId(String sharecode) {
    	return "share-url" + sharecode + "-info";
    }
    //删除redis中存储的“点击邮箱的链接，实现不用密码的登录”的信息
    public boolean removeShareUserToIncrCallChanceInfo(String sharecode) {
        return redis.removeKey(getShareUserToIncrCallChanceInfoId(sharecode));
    }
    
    
    //将用户 sharecode 和 mail 放入redis
    public void setShareUserToIncrCallChanceInfo(String sharecode, String mail, int ttl) {	
    	
    	redis.set(getShareUserToIncrCallChanceInfoId(sharecode), mail, ttl);
    }
    
    //根据sharecode从redis中取出用户邮箱
    public String getShareUserToIncrCallChanceInfo(String sharecode) {
    	String mail = redis.get(getShareUserToIncrCallChanceInfoId(sharecode));	
    	return mail;
    }
    
    
    //根据loginCode判断 是否已经生成对应的链接、链接是否在有效期内
    public boolean hasShareUserToIncrCallChanceInfo(String sharecode) {
    	return redis.hasKey(getShareUserToIncrCallChanceInfoId(sharecode));    	
    }
   //----------------------------------------
    
    
    
//    //根据userId和User将用户信息保存到redis中作为一个信息session
//    public void setUserSession(int userId, User user, int ttl) {
//    	String userJson = JsonUtil.objectToJson(user);
//    	
//    	String sessionId = "userId-" + userId + "-info";
//    	redis.set(sessionId, userJson, ttl);
//    }
//    
//    
//   //根据userId从redis中取出用户
//    public User getUser(int userId) {
//    	String sessionId = "userId-" + userId + "-info";
//    	String userJson = redis.get(sessionId);
//    	//redis里存了3个值么？redis.set(sessionId, userJson, ttl); 通过redis.get(sessionId)得到的是什么？userJson？
//    	
//    	if(userJson == null)
//    		return null;
//    	
//    	return JsonUtil.jsonToPojo(userJson, User.class);
//    }
   
  //根据token和User将用户信息保存到redis中作为一个信息session
    public void setUserSession(String tokenValue, User user, int ttl) {
    	String userJson = JsonUtil.objectToJson(user);
    	
    	String sessionId = "userToken-" + tokenValue + "-info";
    	redis.set(sessionId, userJson, ttl);
    }
    
    //根据token从redis中取出用户
    public User getUser(String tokenValue) {
    	String sessionId = "userToken-" + tokenValue + "-info";
    	String userJson = redis.get(sessionId);
    	
    	if(userJson == null)
    		return null;
    	
    	return JsonUtil.jsonToPojo(userJson, User.class); //返回User类的对象
    }
    
    
    //验证码功能
  //根据用户电话号码和验证码作为用户验证码session
    public void setUserVCode(int phone, String vCode, int ttl) {
    	String sessionId = "user-" + phone + "-vcode";
    	redis.set(sessionId, vCode, ttl);
    }
    
  //根据用户电话号码取出对应的验证码
    public String getUserVCode(int phone) {
    	String sessionId = "user-" + phone + "-vcode";
    	String vCode = redis.get(sessionId);
    	return vCode;
    }
}
