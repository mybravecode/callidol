package com.callidol.service;

import com.callidol.common.CIResult;

public interface BoardService {
    public CIResult getIdolBoardWeekRank(String week);
    
    public CIResult getIdolBoardMonthRank(String month, int page, int size);
    
    public CIResult getIdolBoardYearRank(String year, int page, int size);

    public CIResult getUserBoardWeekRank(String week, long idolId);
    
    public CIResult getUserBoardMonthRank(String month, long idolId, int page, int size) ;
    
    public CIResult getUserBoardYearRank(String year, long idolId, int page, int size);
}
