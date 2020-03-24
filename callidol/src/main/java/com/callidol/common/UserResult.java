package com.callidol.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserResult {
    private Long id;
    
    private String mail;
    
    private String nickName;
    
    private String pic;
    
    private Integer rank;
    
    private Integer call;
    
    private Integer restChance;

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
