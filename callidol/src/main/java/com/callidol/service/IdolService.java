package com.callidol.service;

import com.callidol.common.CIResult;

public interface IdolService {

	public CIResult searchIdolByName(String name);

	public CIResult getIdolInfoById(Long id, Long userId);

}
