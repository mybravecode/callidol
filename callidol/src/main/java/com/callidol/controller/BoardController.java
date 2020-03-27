package com.callidol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.callidol.common.CIResult;
import com.callidol.service.BoardService;


@RestController
@RequestMapping("/apiv1/board")
public class BoardController {
    @Autowired
    private BoardService boardService;
    
    @RequestMapping("/idol/week")
    public CIResult getIdolBoardWeekRank() {
        return boardService.getIdolBoardWeekRank();
    }
    
    
    @RequestMapping("/idol/month")
    public CIResult getIdolBoardMonthRank(@RequestParam Integer page) {
    	if(page <= 0 || page > 5)
    		return CIResult.error("page 不合法 0<page<6");
        return boardService.getIdolBoardMonthRank(page, 10);
    }
    
    
    @RequestMapping("/idol/year")
    public CIResult getIdolBoardYearRank(@RequestParam Integer page) {
    	if(page <= 0 || page > 10)
    		return CIResult.error("page 不合法 0<page<11");
        return boardService.getIdolBoardYearRank(page, 20);
    }
    
    
    @RequestMapping("/user/week")
    public CIResult getUserBoardWeekRank(@RequestParam long idolId) {
        return boardService.getUserBoardWeekRank(idolId);
    }
    
    
    @RequestMapping("/user/month")
    public CIResult getUserBoardMonthRank(@RequestParam long idolId, @RequestParam Integer page) {
    	if(page <= 0 || page > 5)
    		return CIResult.error("page 不合法 0<page<6");
        return boardService.getUserBoardMonthRank(idolId, page, 10);
    }
    
    
    @RequestMapping("/user/year")
    public CIResult getUserBoardYearRank(@RequestParam long idolId, @RequestParam Integer page) {
    	if(page <= 0 || page > 10)
    		return CIResult.error("page 不合法 0<page<11");
        return boardService.getUserBoardYearRank(idolId, page, 20);
    }
    
}
