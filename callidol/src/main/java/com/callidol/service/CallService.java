package com.callidol.service;


import com.callidol.common.CIResult;
import com.callidol.pojo.User;
import com.callidol.utils.DateUtil;

public interface CallService {
    public CIResult getUserRestChance(long userId);

	public CIResult getFreeCallRestChance(long userId);
	
	public CIResult shareToIncrCallChance(User user);

	public CIResult clickShare(String code);
	
	public CIResult callForIdol(long idolId, int callNum, long userId);
	
	public CIResult getUserRankAndScoreForIdol(Long idolId, Long userId);
	
	public void userCallForHisIdol(long userId, long idolId, int callNum, DateUtil dateUtil);
	
}
