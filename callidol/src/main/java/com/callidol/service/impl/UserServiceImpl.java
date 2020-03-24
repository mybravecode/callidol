package com.callidol.service.impl;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.callidol.common.CIResult;
import com.callidol.common.UserResult;
import com.callidol.interceptor.ValidatorInterceptor;
import com.callidol.mapper.UserMapper;
import com.callidol.pojo.User;
import com.callidol.service.UserService;
import com.callidol.utils.CookieUtil;
import com.callidol.utils.HashUtil;
import com.callidol.utils.Mail;
import com.callidol.utils.RandomUtil;
import com.callidol.utils.RedisOp;
import com.callidol.utils.SessionUtil;

import freemarker.core.Macro;


@Service
public class UserServiceImpl implements UserService{
    
	@Value("${spring.service.addr}")
	private String addr;
	
	@Autowired
	private Mail mailService;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private SessionUtil sessionUtil;
	
	@Autowired
	private RedisOp redisOp;
	
	@Override
	public CIResult registerUser(String mail, String nickname, String password) {
		
		
		// 检查用户是否存在
		User userQ = new User();
		userQ.setMail(mail);
		int accCount = userMapper.selectCount(userQ);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>: " + accCount);
		if(accCount > 0) {
			return CIResult.error("账号已经注册，请登录");
		}
		
		String activationCode = HashUtil.hash(mail+"callidol123456789");


		//验证用户是否注册 但是未激活。在redis中验证
		if(sessionUtil.hasActivationRegisterUser(activationCode)) {
			return CIResult.error("账号已经注册，但是未激活，请登录邮箱激活");
		}
			
		//检查是否账号已经注册但是待激活
		//激活连接中存在一个激活码 /apiv1/user/activate?code=ajflafjaf
		
//		String activationCode = RandomUtil.randomStr(15);
		
		
		//将该激活码对应的账号信息暂时缓存到redis中，当激活的时候取出信息并存入mysql中
		//activationCode 作为key   邮箱、密码、用户名作为value
		userQ.setNickname(nickname);
		userQ.setPassword(password);
		sessionUtil.setActivationRegisterUser(activationCode, userQ, 24*60*60); //24h
		
		//
		String activateUrl = "<href>" + addr + "/apiv1/user/activate?code=" + activationCode + "</href>";
		
		// System.out.println(addr);
		
		try {
			mailService.sendHtmlMail(mail, "快乐打榜注册激活链接", "点击该链接" + activateUrl + "可以激活账号，24小时内有效");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return CIResult.exception(e.getMessage());
		}
		return CIResult.ok();
	}

	@Override
	public CIResult activateUser(String code) {
		//根据 “加盐且哈希后的激活码” 查找session，并获取对应的值——user
		User user = sessionUtil.getActivationRegisterUser(code);
		
		if(user == null)
			return CIResult.error("在redis中 查找不到用户信息，找不到激活信息");
		
		
		//
		//将用户保存到用户信息表，完成注册
		try {
			// user.setPassword(HashUtil.hash(user.getPassword()));
			userMapper.insert(user);
			//激活成功后，移除redis中保存的待激活信息
			sessionUtil.removeActivationRegisterUser(code);
			
		}catch (Exception e) {
			return CIResult.exception(e.getMessage());
		}
		
		// System.out.println(">>>>>>>> user.Id: " + user.getId());
		return CIResult.ok("激活成功，请登录");
		
	}

	@Override
	public CIResult loginUser(String mail, String password, HttpServletResponse response) {
		//查询数据库，判断这个用户是否存在
		User userQ = new User();
		userQ.setMail(mail);
		User findUser = userMapper.selectOne(userQ);
		if(findUser == null) {
			return CIResult.error("该用户不存在");
		}
		
		//finUser.getPassword().equals(HashUtil.hash(password)
		if(!findUser.getPassword().equals(password)) {
			return CIResult.error("用户密码输入错误");
		}
		
		setCookieAndSession(findUser, response, 5*60*60);
		return CIResult.ok("用户登录成功");	
	}

	public void setCookieAndSession(User user, HttpServletResponse response, int ttl) {
		//设置cookie 
//		CookieUtil.setCookie(cookieName, cookieValue, expire, response, path);	
		String tokenValue = RandomUtil.randomStr();
		CookieUtil.setCookie(IdString.LoginTokenName, tokenValue, ttl, response, "/");	
		//    public static String LoginTokenName = "user-token";
		
		//设置session
		sessionUtil.setUserSession(tokenValue, user, ttl);
		
	}

	@Override
	public CIResult getUserLoginLink(String mail) {
		//需要生成一个用户可以直接点击登陆的链接
		User userQ = new User();
		userQ.setMail(mail);
		int accCount = userMapper.selectCount(userQ);
		if(accCount == 0) {
			return CIResult.error("该邮箱对应的账号不存在");
		}
		
		String loginCode = HashUtil.hash(mail+"callidol123456789");

		
		//判断是否已经生成过登录链接
		if(sessionUtil.hasUserLoginUrlInfo(loginCode)) {
			return CIResult.error("登录链接已经生成，检查您的邮箱点击登录链接");
		}
		
		
        String loginUrl = "<href>" + addr + "/apiv1/user/elogin?code=" + loginCode + "</href>";
		
		// System.out.println(addr);
		
		try {
			sessionUtil.setUserLoginUrlInfo(loginCode, mail, 5 * 60);
			mailService.sendHtmlMail(mail, "快乐打榜注册登录链接", "点击该链接" + loginUrl + "可以登录账号，5分钟内有效");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return CIResult.exception(e.getMessage());
		}
		
		return CIResult.ok();
	}

	@Override
	public CIResult eloginUserByMail(String loginCode, HttpServletResponse response) {
		String mail = sessionUtil.getUserLoginUrlInfo(loginCode);//根据loginCode从redis中取出用户邮箱
		if(mail == null) {
			return CIResult.error("登陆链接失效，重新获取登录链接，   eloginUserByMail()出错");
		}
		
		User user = new User();
		user.setMail(mail);

		User findUser = userMapper.selectOne(user);
		if(findUser == null) {
			return CIResult.error("该用户不存在");
		}
		
		findUser.setPassword("");
		setCookieAndSession(findUser, response, 5*60*60);
		sessionUtil.removeUserLoginUrlInfo(loginCode);

		return CIResult.ok("用户登录成功");	
	}

	@Override
	public CIResult shareUserToIncrCallChance(User user) {//？？？传User 比 传userId更好吧？
		Long userId = user.getId();
		String mail = user.getMail();
//		public CIResult shareUserToIncrCallChance(String userId) {//？？？传User 比 传userId更好吧？
		//----------------------------获取当前用户
		
		
		//----------------------------获取剩余打榜次数
		//当用户的restChance存在于redis中，直接得到用户的restChance
		//当用户的restChance不存在于redis中，直接将用户的restChance赋值为0
		long restChance = redisOp.hDecrement("RestCallChance", userId.toString(), 0);
		//redisOp.hGet(key, field)  当用户的restChance不存在于redis中，返回为null

		
		//----------------------------生成并发送分享链接到邮箱

		//需要生成一个用户可以直接点击登陆的链接			
		String shareCode = HashUtil.hash(userId+"callidol123456789");

				
				//判断是否已经生成过分享链接
//				if(sessionUtil.hasUserLoginUrlInfo(loginCode)) {
//					return CIResult.error("登录链接已经生成，检查您的邮箱点击登录链接");
//				}
				
				
        String loginUrl = "<href>" + addr + "/apiv1/user/clickToIncrCallChance?code=" + shareCode + "</href>";
		
		// System.out.println(addr);
		
		try {
			
			sessionUtil.setShareUserToIncrCallChanceInfo(shareCode, mail, 5 * 60);
			mailService.sendHtmlMail(mail, "快乐打榜注册登录链接", "点击该链接" + loginUrl + "可以登录账号，5分钟内有效");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return CIResult.exception(e.getMessage());
		}
		HashMap<String, Object> returnMap = new HashMap<>();
		returnMap.put("callChance", restChance);
		returnMap.put("url", loginUrl);
		
		return CIResult.ok("分享专属链接成功",returnMap);
	}

	@Override
	public CIResult clickUserToIncrCallChance(String sharecode) {
		String mail = sessionUtil.getShareUserToIncrCallChanceInfo(sharecode);
		User user = new User();
		user.setMail(mail);
		User shareUser = userMapper.selectOne(user);
		String shareUserId = shareUser.getId().toString();//分享链接的用户
		User currentUser = ValidatorInterceptor.getUser();	
		System.out.println("============="+currentUser);

		//////////////////////////////

        Date date = new Date();
        long currentTime = date.getTime();  
        
		//时间的判断
		//当用户的getCallRestChanceTime存在于redis中，直接得到用户的getCallRestChanceTime
		//当用户的getCallRestChanceTime不存在于redis中，直接将用户的getCallRestChanceTime赋值为0
        
		long clickUserToIncrCallChanceTime = redisOp.hIncrement("RestCallChance-clickUserToIncrCallChanceTime", currentUser.getId().toString(), 0);
		
		if(currentTime - clickUserToIncrCallChanceTime > 60 * 1000) {//1min 为了方便测试
//			if(currentTime - clickUserToIncrCallChanceTime > 6 * 60 * 60 * 1000) {//6小时
			long restChance = redisOp.hIncrement("RestCallChance", shareUserId, 3);
//			UserResult userResult = new UserResult();
//			userResult.setRestChance((int)restChance);
						
			//修改上次获取的时间
			redisOp.hSet("RestCallChance-clickUserToIncrCallChanceTime", currentUser.getId().toString(), currentTime);
			return CIResult.ok("点击专属链接成功","分享者的剩余打榜次数为"+restChance);
		}else {
			long needTime = 6 * 60  - (currentTime - clickUserToIncrCallChanceTime)/(60 * 1000);
			return  CIResult.ok("免费获取打榜次数失败，还没有到6个小时", "还需要"+needTime+"分钟 到达6个小时");
		}
		//////////////////////////////
		
	}
	
}
