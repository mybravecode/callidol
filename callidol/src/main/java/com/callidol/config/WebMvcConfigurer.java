package com.callidol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.callidol.interceptor.ValidatorInterceptor;



@Configuration//表明这是一个适配器
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
	
	@Bean
	ValidatorInterceptor getValidatorInterceptor() {
		return new ValidatorInterceptor();
	}
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		/**
		 * 拦截器按照顺序执行
		 * */
		
		//添加一个身份验证的拦截器,用户在访问该网站时必须先进行身份验证才能访问
		
		registry.addInterceptor(getValidatorInterceptor())
//		.addPathPatterns("/apiv1/user/shareToIncrCallChance")
//		.addPathPatterns("/apiv1/user/clickToIncrCallChance")
		.addPathPatterns("/apiv1/idol/**")
		.addPathPatterns("/apiv1/call/**")
		.addPathPatterns("/apiv1/rank/**")
		.addPathPatterns("/apiv1/tool/**")
		.addPathPatterns("/apiv1/board/**");
//		.addPathPatterns("/collection/**")
//		.addPathPatterns("/index")
//		.addPathPatterns("/");
		
		super.addInterceptors(registry);
	}

}
