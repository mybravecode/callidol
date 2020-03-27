package com.callidol.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResult {
    private Long id;
    
    private String mail;
    
    private String nickName;
    
    private String pic;
    
    //给某个明星打榜排行!!!!
    private Integer rank;
    
    //给某个明星打榜数!!!!
    private Integer call;
    
  //剩余打榜次数
    private Integer restChance;
    
    //专属分享链接
    private String shareUrl;
    
    //上次获取免费打榜次数时间
    private Long lastGetFreeChanceTime;
    
    //上次点击分享链接时间
    private Long lastClickShareChance;
    
    
    //用户给明星打榜次数排名
    public Long getLastGetFreeChanceTime() {
		return lastGetFreeChanceTime;
	}

	public void setLastGetFreeChanceTime(Long lastGetFreeChanceTime) {
		this.lastGetFreeChanceTime = lastGetFreeChanceTime;
	}

	public Long getLastClickShareChance() {
		return lastClickShareChance;
	}

	public void setLastClickShareChance(Long lastClickShareChance) {
		this.lastClickShareChance = lastClickShareChance;
	}

	
    
    
	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getCall() {
		return call;
	}

	public void setCall(Integer call) {
		this.call = call;
	}

	public Integer getRestChance() {
		return restChance;
	}

	public void setRestChance(Integer restChance) {
		this.restChance = restChance;
	}

    
}
