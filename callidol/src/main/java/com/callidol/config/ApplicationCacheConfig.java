package com.callidol.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callidol.common.BoardCache;


@EnableCaching
@Configuration
public class ApplicationCacheConfig{
    
	@Bean
	public CacheManager cacheManager() {
	    ApplicationCacheManager cacheManager = new ApplicationCacheManager();
	    cacheManager.setExpire(BoardCache.IdolWeekBoardCache, 30);
	    
	    return cacheManager;
	}
	
}
