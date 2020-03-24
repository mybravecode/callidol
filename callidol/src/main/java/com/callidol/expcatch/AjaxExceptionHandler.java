package com.callidol.expcatch;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.callidol.common.CIResult;


@RestControllerAdvice
public class AjaxExceptionHandler {
    
    @ExceptionHandler(value=Exception.class)
	public CIResult ajaxErrorHandler(HttpServletRequest req, Exception e)
			throws Exception{
		e.printStackTrace();
		// System.out.println("an error of ajax occured");
		return CIResult.exception(e.getMessage());
	}
}
