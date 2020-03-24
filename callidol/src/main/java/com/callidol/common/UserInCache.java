package com.callidol.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.callidol.utils.HashUtil;
import com.callidol.utils.RedisOp;


@Component
public class UserInCache {
	@Value("${spring.service.addr}")
	private String addr;
	
    @Autowired
    private RedisOp redisOp;
    
    public static String LastGetFreeChanceTime = "LastGetFreeChanceTime";
    
    public static String LastClickShareTime = "LastClickShareTime";
    
    public static String RestCallChance = "RestChance";
    
    public static String ShareUrl = "ShareUrl";
    

    //根据用户id  得到第一个key:"user-result" + userId
    private static String getUserMapName(long userId) {
        return "user-result" + userId;	
    }    
    
    //---------------------RestCallChance 当前剩余打榜次数
    //当前剩余打榜次数
    public int getRestCallChance(long userId) {
    	return (int) redisOp.hIncrement(getUserMapName(userId), RestCallChance, 0);
    	//第一个key:"user-result" + userId  第二个key:RestCallChance  第二个key对应的值：value + 0
    }
    
    //增加 当前剩余打榜次数
    public int addRestCallChance(long userId, int addDelta) {
    	return (int) redisOp.hIncrement(getUserMapName(userId), RestCallChance, addDelta); 
    }
    //减少 当前剩余打榜次数
    public int descRestCallChance(long userId, int addDelta) {
    	return (int) redisOp.hIncrement(getUserMapName(userId), RestCallChance, -addDelta);
    }
    
    
    //---------------------LastGetFreeChanceTime 获得最后一次免费打榜的时间
    public long getLastGetFreeChanceTime(long userId) { //毫秒
    	return redisOp.hIncrement(getUserMapName(userId), LastGetFreeChanceTime, 0);
    }
    
    public void setLastGetFreeChanceTime(long userId, long currentTime) {
    	redisOp.hSet(getUserMapName(userId), LastGetFreeChanceTime, currentTime);
    }
    
    
    //---------------------ShareUrl 获取某个用户的专属分享链接
    public String genShareUrl(long userId) {
    	String shareCode = HashUtil.hash(userId+"callidol123456789");
    	return "<href>" + addr + "/apiv1/user/clickShare?code=" + shareCode + "</href>";
    }
    
    
    //---------------------LastClickShareTime 最后一次 点击 其他用户分享链接的时间
    public long getLastClickShareTime(long userId) { //毫秒
    	return redisOp.hIncrement(getUserMapName(userId), LastClickShareTime, 0);
    }
    
    public void setLastClickShareTime(long userId, long currentTime) {
    	redisOp.hSet(getUserMapName(userId), LastClickShareTime, currentTime);
    }
    
    

    
    
}
