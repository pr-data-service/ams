package com.drps.ams.service.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.cache.FlatDetailsCacheService;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.service.FlatDetailsService;

@Service
public class FlatDetailsHelperService {
	
	private static final Logger logger = LogManager.getLogger(FlatDetailsHelperService.class);
	
	@Autowired
	FlatDetailsService flatDetailsService;
	
	public String getFlatNo(Long apartmentId, Long flatId, Object ...arr) {
		String flatNo = null; 
		if(arr != null && arr.length > 0) {
			flatNo = String.valueOf(arr[0]);
		} else {
			FlatDetailsEntity flatDetailsEntity = flatDetailsService.findById(apartmentId, flatId);
			flatNo = flatDetailsEntity != null ? flatDetailsEntity.getFlatNo() : "";
		}
		return flatNo;
	}
}
