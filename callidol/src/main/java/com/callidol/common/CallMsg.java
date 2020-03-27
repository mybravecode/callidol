package com.callidol.common;

public class CallMsg {
    private long userId;
        
    private long idolId;
    
    private int callNum;
    
    private long time;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getIdolId() {
		return idolId;
	}

	public void setIdolId(long idolId) {
		this.idolId = idolId;
	}

	public int getCallNum() {
		return callNum;
	}

	public void setCallNum(int callNum) {
		this.callNum = callNum;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
    
    
}
