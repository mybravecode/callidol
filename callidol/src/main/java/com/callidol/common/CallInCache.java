package com.callidol.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.callidol.utils.DateUtil;
import com.callidol.utils.RedisOp;


@Component
public class CallInCache {
	@Autowired
    private RedisOp redisOp;
	
	public final static String Week = "week";
	public final static String Month = "month";
	public final static String Year = "year";
	
	// key : idol-{boardType}-board-{week}
	//value: 明星 和 明星的被打榜次数
	//产生明星榜单
	public static final String genIdolBoardId(String boardType, String timeValue) {
		return String.format("idol-%s-board-%s", boardType, timeValue);
		//idol-week-board-202013
	}
	
	// key : user-{idolId}-{boardType}-board-{week} 
	//value: userId 和 用户的打榜次数
	//产生某个明星下 用户排名分数的榜单
	public static final String genUserBoardId(Long idolId, String boardType, String timeValue) {
		return String.format("user-%s-%s-board-%s", idolId, boardType, timeValue);
		//user-133-week-board-202013
	}
	
	//增加某个明星的打榜数(周榜)
//	public int incrIdoWeeklCallNum(long idolId, int callNum) {
//		// genIdolBoardId(idolId, Week);
//	}
	
	
	public void incrIdolCallNum(long idolId, int callNum, DateUtil dateUtil) {
	    //增加明星打榜数
		//增加周榜
		String weekKey = genIdolBoardId(Week, dateUtil.getWeek());
		redisOp.zincr(weekKey, idolId, callNum);
		//增加月榜
		String monthKey = genIdolBoardId(Month, dateUtil.getMonth());
		redisOp.zincr(monthKey, idolId, callNum);
		//增加年榜
		String yearKey = genIdolBoardId(Year, dateUtil.getYear());
		redisOp.zincr(yearKey, idolId, callNum);
	}//
	
	
	public void incrUserCallNum(long idolId, Long userId, int callNum, DateUtil dateUtil) {
	    //增加某个用户相对明星的打榜次数
		//增加周榜
		String weekKey = genUserBoardId(idolId, Week, dateUtil.getWeek());
		redisOp.zincr(weekKey, userId, callNum);
		
		//增加月榜
		String monthKey = genUserBoardId(idolId, Month, dateUtil.getMonth());
		redisOp.zincr(monthKey, userId, callNum);
		
		//增加年榜
		String yearKey = genUserBoardId(idolId, Year, dateUtil.getYear());
		redisOp.zincr(yearKey, userId, callNum);
	}//
	
	public RankAndScore getIdolRankAndScoreByYear(long idolId, String timeValue) {
		return redisOp.getRankAndScore(genIdolBoardId(Week, timeValue), idolId);
	}
	
	public RankAndScore getUserRankAndScoreForIdolByWeek(long userId, Long idolId, String timeValue) {
		return redisOp.getRankAndScore(genUserBoardId(idolId, Week, timeValue), userId);
	}
}
