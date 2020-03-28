package com.callidol.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.bytecode.Mnemonic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.callidol.common.BoardCache;
import com.callidol.common.CIResult;
import com.callidol.common.CallInCache;
import com.callidol.common.IdolResult;
import com.callidol.common.RankAndScore;
import com.callidol.common.RankBoardResult;
import com.callidol.common.UserResult;
import com.callidol.mapper.IdolMapper;
import com.callidol.mapper.UserMapper;
import com.callidol.pojo.Idol;
import com.callidol.pojo.User;
import com.callidol.service.BoardService;
import com.callidol.utils.DateUtil;
import com.callidol.utils.JsonUtil;
import com.callidol.utils.RedisOp;
import com.github.pagehelper.Page;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class BoardServiceImpl implements BoardService{
    @Autowired
    private CallInCache callInCache;
    
    @Autowired
    private RedisOp redisOp;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private IdolMapper idolMapper;
    
    //根据boardId从明星排行榜中取出排名在[start,end]之间的明星和信息
    private RankBoardResult<IdolResult> getIdolRankBoardInfo(String boardId, int start, int end) {
		
		//rankAndScoreList中的id 装入longIdList
		List<RankAndScore> rankAndScoreList = redisOp.reverseRangeWithScores(boardId, start, end);
		List<Long> longIdList = new ArrayList<>(rankAndScoreList.size());
		for(RankAndScore rankAndScore: rankAndScoreList)
			longIdList.add(Long.parseLong(rankAndScore.getId()));
		
		//取出longIdLists中id对应   的idolList
		Example example = new Example(Idol.class);
		Criteria criteria = example.createCriteria();
		criteria.andIn("id", longIdList);
		List<Idol> idolList = idolMapper.selectByExample(example);
		//连接rankAndScoreList 和 idolList
		Map<String, Idol> idolMap = new HashMap<String, Idol>(idolList.size());
		for(Idol idol: idolList)
			idolMap.put(idol.getId().toString(), idol);
		
		List<IdolResult> idolResultList = new ArrayList<>(idolMap.size());
		
		
		for(RankAndScore rankAndScore: rankAndScoreList) {
			IdolResult idolResult = new IdolResult();
			idolResult.setId(Long.parseLong(rankAndScore.getId()));
			idolResult.setCalled(rankAndScore.getIntScore());
			idolResult.setRank(rankAndScore.getRank());
			
			Idol idol = idolMap.get(rankAndScore.getId());
			if(idol == null)
				continue;
			
			idolResult.setBrief(idol.getBrief());
			idolResult.setPic(idol.getPic());
			idolResult.setName(idol.getName());
			idolResultList.add(idolResult);
		}
		
		RankBoardResult<IdolResult> rankBoardResult = new RankBoardResult<>();
		rankBoardResult.setRankList(idolResultList);
		return rankBoardResult;
    }
    
    private RankBoardResult<UserResult> getUserRankBoardInfo(String boardId, int start, int end) {
		
		//rankAndScoreList中的id 装入longIdList
		List<RankAndScore> rankAndScoreList = redisOp.reverseRangeWithScores(boardId, start, end);
		List<Long> longIdList = new ArrayList<>(rankAndScoreList.size());
		for(RankAndScore rankAndScore: rankAndScoreList)
			longIdList.add(Long.parseLong(rankAndScore.getId()));
		
		//取出longIdLists中id对应   的userList
		Example example = new Example(User.class);
		Criteria criteria = example.createCriteria();
		criteria.andIn("id", longIdList);
		List<User> userList = userMapper.selectByExample(example);
		//连接rankAndScoreList 和 userList
		Map<String, User> userMap = new HashMap<String, User>(userList.size());
		for(User user: userList)
			userMap.put(user.getId().toString(), user);
		
		List<UserResult> userResultList = new ArrayList<>(userMap.size());
		
		
		for(RankAndScore rankAndScore: rankAndScoreList) {
			UserResult userResult = new UserResult();
			userResult.setId(Long.parseLong(rankAndScore.getId()));
			userResult.setCall(rankAndScore.getIntScore());
			userResult.setRank(rankAndScore.getRank());
			
			User user = userMap.get(rankAndScore.getId());
			if(user == null)
				continue;

			userResult.setNickName(user.getNickname());
			userResult.setPic(user.getPic());
			userResult.setMail(user.getMail());
			userResultList.add(userResult);
	
		}
		
		RankBoardResult<UserResult> rankBoardResult = new RankBoardResult<>();
		rankBoardResult.setRankList(userResultList);
		return rankBoardResult;
    }
    
    
    //hashMap 
	@Override
	@Cacheable(value=BoardCache.IdolWeekBoardCache, key="#week")
	public CIResult getIdolBoardWeekRank(String week) {
		//从本周排行榜中取出排名在1-20的明星
        String boardId = CallInCache.genIdolBoardId(CallInCache.Week, week);
		RankBoardResult< IdolResult> rankBoardResult = getIdolRankBoardInfo(boardId, 1, 20);
		rankBoardResult.setMore(0);
		return CIResult.ok("OK", rankBoardResult);
		
	}

	@Override
	@Cacheable(value=BoardCache.IdolMonthBoardCache, key="#month + #page + #size")
	public CIResult getIdolBoardMonthRank(String month, int page, int size) {
		int start = (page - 1) * size + 1;
		int end = start + size - 1;
//        String boardId = CallInCache.genIdolBoardId(CallInCache.Month, new DateUtil().getMonth());
        String boardId = CallInCache.genIdolBoardId(CallInCache.Month, month);

		long total = redisOp.zCount(boardId);
		
		if(total < start) {
			return CIResult.error("没有该页数据");
		}
		
		RankBoardResult<IdolResult> rankBoardResult = getIdolRankBoardInfo(boardId, start, end);
		
		if(end >= 50 ||total <= end) {
			rankBoardResult.setMore(0);
		}
		else {
			rankBoardResult.setMore(1);
		}
		
		return CIResult.ok("OK", rankBoardResult);

	}

	@Override
	@Cacheable(value=BoardCache.IdolYeayBoardCache, key="#year + #page + #size")
	public CIResult getIdolBoardYearRank(String year, int page, int size) {
		int start = (page - 1) * size + 1;
		int end = start + size - 1;
//        String boardId = CallInCache.genIdolBoardId(CallInCache.Year, new DateUtil().getYear());
        String boardId = CallInCache.genIdolBoardId(CallInCache.Year, year);

		long total = redisOp.zCount(boardId);
		
		if(total < start) {
			return CIResult.error("没有该页数据");
		}
		
		RankBoardResult<IdolResult> rankBoardResult = getIdolRankBoardInfo(boardId, start, end);
		
		if(end >= 200 ||total <= end) {
			rankBoardResult.setMore(0);
		}
		else {
			rankBoardResult.setMore(1);
		}
		
		return CIResult.ok("OK", rankBoardResult);

	}
    
	
	@Override
	@Cacheable(value=BoardCache.UserWeekBoardCache, key="#week")
	public CIResult getUserBoardWeekRank(String week, long idolId) {
		//从本周排行榜中取出给idolId打榜排名在1-20的用户
//		String boardId = CallInCache.genUserBoardId(idolId, CallInCache.Week, new DateUtil().getWeek());
		String boardId = CallInCache.genUserBoardId(idolId, CallInCache.Week, week);
		
		RankBoardResult<UserResult> rankBoardResult = getUserRankBoardInfo(boardId, 1, 20);
		rankBoardResult.setMore(0);
		return CIResult.ok("OK", rankBoardResult);
	}


	@Override
	@Cacheable(value=BoardCache.UserMonthBoardCache, key="#month + #page + #size")
	public CIResult getUserBoardMonthRank(String month, long idolId, int page, int size) {
		int start = (page - 1) * size + 1;
		int end = start + size - 1;
//		String boardId = CallInCache.genUserBoardId(idolId, CallInCache.Month, new DateUtil().getMonth());
		String boardId = CallInCache.genUserBoardId(idolId, CallInCache.Month, month);
		/////////////////
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+boardId);

		long total = redisOp.zCount(boardId);
		
		if(total < start) {
			return CIResult.error("没有该页数据");
		}
		
		RankBoardResult<UserResult> rankBoardResult = getUserRankBoardInfo(boardId, start, end);
		
		if(end >= 50 ||total <= end) {
			rankBoardResult.setMore(0);
		}
		else {
			rankBoardResult.setMore(1);
		}
		
		return CIResult.ok("OK", rankBoardResult);

	}

	@Override
	@Cacheable(value=BoardCache.UserYearBoardCache, key="#year + #page + #size")
	public CIResult getUserBoardYearRank(String year, long idolId, int page, int size) {
		int start = (page - 1) * size + 1;
		int end = start + size - 1;
//        String boardId = CallInCache.genUserBoardId(idolId, CallInCache.Year, new DateUtil().getYear());
        String boardId = CallInCache.genUserBoardId(idolId, CallInCache.Year, year);

		long total = redisOp.zCount(boardId);
		
		if(total < start) {
			return CIResult.error("没有该页数据");
		}
		
		RankBoardResult<UserResult> rankBoardResult = getUserRankBoardInfo(boardId, start, end);
		
		if(end >= 200 ||total <= end) {
			rankBoardResult.setMore(0);
		}
		else {
			rankBoardResult.setMore(1);
		}
		
		return CIResult.ok("OK", rankBoardResult);
	}
}
