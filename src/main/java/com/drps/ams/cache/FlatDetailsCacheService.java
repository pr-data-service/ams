package com.drps.ams.cache;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.join.FlatDetailsAndUserDetailsEntity;
import com.drps.ams.service.FlatDetailsService;

/*
 * Ref 1: https://buddhiprabhath.medium.com/spring-boot-crud-example-with-caching-c2c9b7fd8f05
 * Ref 2: https://www.techgeeknext.com/spring-boot/spring-boot-caching
 * Ref 3: https://howtodoinjava.com/spring-boot/spring-boot-cache-example/
 * 
 */

@Service
public class FlatDetailsCacheService {
	
	private static final Logger logger = LogManager.getLogger(FlatDetailsCacheService.class);
	
	@Autowired
	FlatDetailsService flatDetailsService;
	
	private final String CACHE_NAME = "FlatDetailsAndUserDetailsEntity";
	private final String CACHE_KEY = "#apartmentId";

	@Cacheable(cacheNames = CACHE_NAME, key = CACHE_KEY)
	public Map<Long, FlatDetailsAndUserDetailsEntity> getFlatDetailsAndUserDetailsMapByApartmentId(Long apartmentId) {
		logger.info("getting FlatDetailsAndUserDetailsEntity from database.................");
		Map<Long, FlatDetailsAndUserDetailsEntity> map = flatDetailsService.getFlatDetailsAndUserDetailsMap(apartmentId);
		return map;
	}
	
	//@CachePut(cacheNames = CACHE_NAME, key = CACHE_KEY)
	public void updateCacheByApartmentId(Long apartmentId) {
		//pending implementation.....
	}
	
	@Caching(evict = { @CacheEvict(cacheNames=CACHE_NAME, key = CACHE_KEY)})
	public void removeCacheByApartmentId(Long apartmentId) {
		//Removed cache
	}

}
