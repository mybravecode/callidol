package com.callidol.service;

import com.callidol.common.CIResult;

public interface BoardService {
    public CIResult getIdolBoardWeekRank();
    
    public CIResult getIdolBoardMonthRank(int page, int size);
    
    public CIResult getIdolBoardYearRank(int page, int size);

    public CIResult getUserBoardWeekRank(long idolId);
    
    public CIResult getUserBoardMonthRank(long idolId, int page, int size) ;
    
    public CIResult getUserBoardYearRank(long idolId, int page, int size);
}
