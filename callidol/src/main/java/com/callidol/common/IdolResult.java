package com.callidol.common;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdolResult {
	private Long id;

    private String name;
    
    private String brief;

    private String pic;
    
    private Integer called;
    
    private Integer rank;

    private Integer userRank;
    
    public Integer getUserRank() {
		return userRank;
	}

	public void setUserRank(Integer userRank) {
		this.userRank = userRank;
	}

	public Integer getUserCall() {
		return userCall;
	}

	public void setUserCall(Integer userCall) {
		this.userCall = userCall;
	}

	private Integer userCall;
    
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getCalled() {
		return called;
	}

	public void setCalled(Integer called) {
		this.called = called;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
    
    
//    private int rankYear;
//    
//    private int rankWeek;
//    
//    private int rankMonth;
//    
//    private int calledYear;
//    
//    private int calledWeek;
//    
//    private int calledMonth;
    
    
}
