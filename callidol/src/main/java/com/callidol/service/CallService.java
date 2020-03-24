package com.callidol.service;

import com.callidol.common.CIResult;

public interface CallService {
    public CIResult getUserRestChance(String userId);

	public CIResult getCallRestChance(String userId);
}
