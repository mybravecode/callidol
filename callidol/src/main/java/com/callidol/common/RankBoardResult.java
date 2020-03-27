package com.callidol.common;

import java.util.List;


public class RankBoardResult <T>{
	
    private List<T> rankList ;
    
    private Integer more;

	public List<T> getRankList() {
		return rankList;
	}

	public void setRankList(List<T> rankList) {
		this.rankList = rankList;
	}

	public Integer getMore() {
		return more;
	}

	public void setMore(Integer more) {
		this.more = more;
	}
    
}
