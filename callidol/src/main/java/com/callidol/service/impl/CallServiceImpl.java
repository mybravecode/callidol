package com.callidol.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import com.callidol.common.CIResult;
import com.callidol.common.CallInCache;
import com.callidol.common.CallMsg;
import com.callidol.common.RankAndScore;
import com.callidol.common.UserInCache;
import com.callidol.common.UserResult;
import com.callidol.interceptor.ValidatorInterceptor;
import com.callidol.mapper.IdolMapper;
import com.callidol.mapper.UserMapper;
import com.callidol.pojo.Idol;
import com.callidol.pojo.User;
import com.callidol.service.CallService;
import com.callidol.utils.DateUtil;
import com.callidol.utils.HashUtil;
import com.callidol.utils.SessionUtil;


@Service
public class CallServiceImpl implements CallService{
	
	
	@Value("${spring.service.addr}")
	private String addr;
		
	@Autowired
	private SessionUtil sessionUtil;
		
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserInCache userInCache;
	
	@Autowired
	private IdolMapper idolMapper;
	
	
	@Autowired
	private CallInCache callInCache;
	
	
	@Override
	public CIResult getUserRestChance(long userId) {

		//当用户的restChance存在于redis中，直接得到用户的restChance
		//当用户的restChance不存在于redis中，直接将用户的restChance赋值为0
		// long restChance = redisOp.hDecrement("RestCallChance", userId, 0);
		//redisOp.hGet(key, field)  当用户的restChance不存在于redis中，返回为null
		
		int restChance = userInCache.getRestCallChance(userId);
		UserResult userResult = new UserResult();
		userResult.setRestChance(restChance);
		return CIResult.ok("ok", userResult);//只需要返回restChance  "callChance": 24
	}

	@Override
	public CIResult getFreeCallRestChance(long userId) {
        long currentTime = new Date().getTime();  
        
		//时间的判断
		//当用户的getCallRestChanceTime存在于redis中，直接得到用户的getCallRestChanceTime
		//当用户的getCallRestChanceTime不存在于redis中，直接将用户的getCallRestChanceTime赋值为0
		//long getFreeCallRestChanceTime = redisOp.hIncrement("RestCallChance-getFreeCallRestChanceTime", userId, 0);
		long lastGetFreeTime = userInCache.getLastGetFreeChanceTime(userId);
		
		long internal = currentTime - lastGetFreeTime;
		if( internal < 6 * 60 * 60 * 1000) {//失败
//			long needTime = 6 * 60  - (currentTime - getFreeCallRestChanceTime)/(60 * 1000);
			return  CIResult.error("免费获取打榜次数失败，还没有到6个小时, 还需要"+ ((6 * 60 * 60 * 1000 - internal) / 60000.0)+"分钟到达6个小时");
		}
		//成功
		//long restChance = redisOp.hIncrement("RestCallChance", userId, 3);
		int restChance = userInCache.addRestCallChance(userId, 5);
		UserResult userResult = new UserResult();
		userResult.setRestChance(restChance);
//		System.out.println("=======第一次打印:currentTime:"+currentTime+"...getFreeCallRestChanceTime"+getFreeCallRestChanceTime);			
		//修改上次获取的时间
		//redisOp.hSet("RestCallChance-getFreeCallRestChanceTime", userId, currentTime);
		userInCache.setLastGetFreeChanceTime(userId, currentTime);
//			
//		getFreeCallRestChanceTime = redisOp.hIncrement("RestCallChance-getFreeCallRestChanceTime", userId, 0);
//		System.out.println("=======第二次打印：currentTime:"+currentTime+"...getFreeCallRestChanceTime"+getFreeCallRestChanceTime);			

		return  CIResult.ok("免费获取打榜次数成功", userResult);
	}
    
	
	@Override
	public CIResult shareToIncrCallChance(User user) {
		Long userId = user.getId();
		
		//需要生成一个用户可以直接点击登陆的链接			
		String shareCode = HashUtil.hash(userId+"callidol123456789");			
        String shareUrl = "<href>" + addr + "/apiv1/call/clickShare?code=" + shareCode + "</href>";
		
        sessionUtil.setShareUserToIncrCallChanceInfo(shareCode, userId, 5 * 60);
        
        
        //----------------------------获取剩余打榜次数
		//redisOp.hGet(key, field)  当用户的restChance不存在于redis中，返回为null
      	//当用户的restChance存在于redis中，直接得到用户的restChance
      	//当用户的restChance不存在于redis中，直接将用户的restChance赋值为0
//        long restChance = redisOp.hDecrement("RestCallChance", userId, 0);
      	int restChance = userInCache.getRestCallChance(userId);
      	
		UserResult userResult = new UserResult();
		userResult.setShareUrl(shareUrl);
		userResult.setRestChance(restChance);
		return CIResult.ok("分享专属链接成功", userResult);
	}

	@Override
	public CIResult clickShare(String sharecode) {
		long userId = sessionUtil.getShareUserToIncrCallChanceInfo(sharecode);
		User shareUser = userMapper.selectByPrimaryKey(userId);
		//判断该分享码对应的用户是否存在
		if(shareUser == null) {
			return CIResult.error("无效分享码");
		}
		
		long shareUserId = shareUser.getId();//分享链接的用户
		
		User currentUser = ValidatorInterceptor.getUser();	
		//System.out.println("============="+currentUser);


        // Date date = new Date();
        long currentTime = new Date().getTime();  
        
		//时间的判断
		//当用户的getCallRestChanceTime存在于redis中，直接得到用户的getCallRestChanceTime
		//当用户的getCallRestChanceTime不存在于redis中，直接将用户的getCallRestChanceTime赋值为0
        
//		long clickUserToIncrCallChanceTime = redisOp.hIncrement("RestCallChance-clickUserToIncrCallChanceTime", currentUser.getId().toString(), 0);
//		
//		if(currentTime - clickUserToIncrCallChanceTime < 60 * 1000) {//1min 为了方便测试
////			if(currentTime - clickUserToIncrCallChanceTime < 6 * 60 * 60 * 1000) {//6小时
//	        long needTime = 6 * 60  - (currentTime - clickUserToIncrCallChanceTime)/(60 * 1000);
//			return  CIResult.ok("免费获取打榜次数失败，还没有到6个小时", "还需要"+needTime+"分钟 到达6个小时");
//		}
//		
		long internal = currentTime - userInCache.getLastClickShareTime(currentUser.getId());
		
		//对比点击者上次点击分享链接时间
		if(internal < 60 * 1000) {
			return  CIResult.ok("免费获取打榜次数失败，还没有到6个小时", "还需要"+(60 * 1000 - internal / 60000.0)+"分钟 到达6个小时");
		}
		
		//增加链接分享者剩余次数
		int restChance = userInCache.addRestCallChance(shareUserId, 20);
						
		//修改上次获取的时间 哪里
	    //redisOp.hSet("RestCallChance-clickUserToIncrCallChanceTime", currentUser.getId(), currentTime);
		
		//重置点击链接的时间为当前时间
		userInCache.setLastClickShareTime(currentUser.getId(), currentTime);
		return CIResult.ok("点击专属链接成功","分享者的剩余打榜次数为"+restChance);
		
	}

	@Override
	public CIResult callForIdol(long idolId, int callNum, long userId) {
		
		//1.判断明星对应id是否存在
		Idol idol = idolMapper.selectByPrimaryKey(idolId); //null
		
		if(idol == null)
			return CIResult.error("明星id:" + idolId + "不存在");
        
		//2.判断是否还有剩余次数并且减去次数
		//
		
//		int restChance = userInCache.descRestCallChance(userId, callNum);
		
//		{"sucess": 1, "rest": 22}
//		UserResult userResult = new UserResult();
//		
//		if(restChance < 0) {
//			userInCache.addRestCallChance(userId, callNum);
//			userResult.setRestChance(restChance + callNum);
//			return CIResult.error("打榜剩余票数不够，请重新输入打榜次数", userResult);
//		}
		
		//如果打榜成功则返回的restChance >=0 ，否则返回的restChance < 0且-restChance为剩余打榜次数
		
		int restChance = userInCache.queryAndSubRestChanceByCallNum(userId, callNum);
		UserResult userResult = new UserResult();
		
		
		
        if(restChance < 0) {
        	if(restChance == -200)
        		restChance = 0;
        	
        	//得到某个明星的榜单下，用户的打榜次数和排名
        	RankAndScore userRankAndScore = callInCache.getUserRankAndScoreForIdolByWeek(userId, idolId, new DateUtil().getWeek());
			userResult.setRestChance(-restChance);
			userResult.setCall(userRankAndScore.getScore().intValue());//打榜次数
			userResult.setRank(userRankAndScore.getRank());//排名
			return CIResult.error("打榜剩余票数不够，请重新输入打榜次数", userResult);
		}
        
		//1 2 3 4 
		
		//3.增加明星对应的票数
		//增加明星周榜月榜年榜  以及  某个明星打榜的周榜月榜年榜的用户排名
		//避免代码太长太多单独分一个函数出来
		
//		CallMsg callMsg = new CallMsg();
//		
//		long time = new DateUtil().getTimeMillis();
//		callMsg.setTime(time);
//		
//		callMsg.setCallNum(callNum);
//		callMsg.setIdolId(idolId);
//		callMsg.setUserId(userId);
		
		RankAndScore userRankAndScore = callInCache.getUserRankAndScoreForIdolByWeek(userId, idolId, new DateUtil().getWeek());
		userResult.setRestChance(restChance);
		userResult.setCall(userRankAndScore.getScore().intValue());//打榜次数
		userResult.setRank(userRankAndScore.getRank());//排名
		
		// callInCache.setUserCallMsg(callMsg);
	    userCallForHisIdol(userId, idolId, callNum, new DateUtil());
		
		return CIResult.ok("打榜成功", userResult);
	}
	
	@Override
	public void userCallForHisIdol(long userId, long idolId, int callNum, DateUtil dateUtil) {
//		DateUtil dateUtil = new DateUtil();
		callInCache.incrIdolCallNum(idolId, callNum, dateUtil);
		callInCache.incrUserCallNum(idolId, userId, callNum, dateUtil);
	}

	@Override
	public CIResult getUserRankAndScoreForIdol(Long idolId, Long userId) {
	    RankAndScore rankAndScore = callInCache.getUserRankAndScoreForIdolByWeek(userId, idolId, new DateUtil().getWeek());
	    UserResult userResult = new UserResult();
	    userResult.setCall(rankAndScore.getIntScore());
	    userResult.setRank(rankAndScore.getRank());
	    return CIResult.ok("OK", userResult);
	}
	
}
