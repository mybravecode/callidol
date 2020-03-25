package com.callidol.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.callidol.common.CallInCache;
import com.callidol.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService{
    @Autowired
    private CallInCache callInCache;
    
    
    
}
