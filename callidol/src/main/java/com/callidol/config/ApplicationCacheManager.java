package com.callidol.config;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.lang.Nullable;

public class ApplicationCacheManager extends ConcurrentMapCacheManager{
	
    
	/**
	 * Construct a dynamic ConcurrentMapCacheManager,
	 * lazily creating cache instances as they are being requested.
	 */
	public ApplicationCacheManager() {
	}

	/**
	 * Construct a static ConcurrentMapCacheManager,
	 * managing caches for the specified cache names only.
	 */
	public ApplicationCacheManager(String... cacheNames) {
		super(cacheNames);
	}
	
	
	private final Map<String, Integer> expires = new HashMap<String, Integer>();
	
	private final Map<String, Integer> cacheSetTime = new ConcurrentHashMap();
	
	
	public void setExpire(String name, int expire) {
	    expires.put(name, expire);	
	}
	
	@Override
	@Nullable
	public Cache getCache(String name) {
		
		//
		Cache cache = super.getCache(name);
		
		Integer expire = expires.get(name);
		
		Integer putTime = cacheSetTime.get(name);
		
		int currentT = new Date().getSeconds();
		//没有过期时间
		if(expire == null)
			return cache;
		
		if( putTime == null) {
			cacheSetTime.put(name, currentT);
			return cache;
		}
		
		//还没过期
		if(putTime + expire > currentT)
			return cache;

		//name对应的cache已经过期
		cache.clear();
		
		
		cacheSetTime.put(name, currentT);
		return cache;
	}
	
}
