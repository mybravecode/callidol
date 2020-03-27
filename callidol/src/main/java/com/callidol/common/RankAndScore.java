package com.callidol.common;

import com.callidol.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankAndScore {
	
    private Integer rank;
    
    private Double score;
   
    private String id;
    
    public Integer getIntScore() {
    	return this.score.intValue();
    }
    
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getId() {
		return id;
	}

	public void setIdolId(String id) {
		this.id = id;
	}
    
	public String toString() {
		return JsonUtil.objectToJson(this);
	}
	
    
}
