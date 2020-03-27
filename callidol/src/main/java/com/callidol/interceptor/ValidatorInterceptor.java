package com.callidol.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.callidol.common.CIResult;
import com.callidol.pojo.User;
import com.callidol.service.impl.IdString;
import com.callidol.utils.CookieUtil;
import com.callidol.utils.JsonUtil;
import com.callidol.utils.SessionUtil;




public class ValidatorInterceptor implements HandlerInterceptor{
    
	@Autowired private SessionUtil session ;
	
	// 定义一个本地线程变量，存放登录用户
    private static final ThreadLocal<User> tl = new ThreadLocal<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		
//		System.out.println("调用用户身份验证拦截器");
//		System.out.println("session: " + session);
		
        String token = CookieUtil.getCookie(IdString.LoginTokenName, request);
		
		if(token == null) {
			returnJson(response, CIResult.error("用户token验证失败（为空）"));
			return false;
			 //TODO 返回CIResult
			//throw new Exception("用户token验证失败（为空）");
		}
		
		//1   2    3   4
		//--------------------
//		System.out.println("token:>>>>>>>>>>" + token);
		User user = session.getUser(token);
//		System.out.println("ValidatorInterceptor=====================user, session: "+user);
		if(user == null) {
			//session失效也要重新登录
			//返回CIResult
			//throw new Exception("登录失效，请重新登录");
			
			//把错误信息写到response 在客户端（比如浏览器）显示
			returnJson(response, CIResult.error("登录失效，请重新登录"));
			return false;
		}
		
		//身份验证通过
		
		//System.out.println("业务处理前!!!!!!!!!");
		
		//如何将user对象继续下传，给业务逻辑使用，避免再次根据cookie查询用户信息？？？？？？
		//1 将user放到request中 request.setAttribute("user-session", user); 不是特别推荐修改request
		
		//2 本地线程变量
		//在这里将user放入该线程的本地内存中
		tl.set(user);
		
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		tl.remove();
		
		//清除线程变量的内容，防止内存泄漏
		
		// TODO Auto-generated method stub
//		System.out.println("业务处理完毕!!!!!!!!!!!!");
	}
	
	public static User getUser() {
		return tl.get();
	}//对外提供getUser方法获取放置的user对象
    
	
	private void returnJson(HttpServletResponse response, CIResult result) throws IOException{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8"); //text/html
        try {
            writer = response.getWriter();
           
            writer.print(result);
            
        } catch (IOException e){
            // LoggerUtil.logError(ECInterceptor.class, "拦截器输出流异常"+e);
        	throw e;
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }

}
