package com.drps.ams.service;

import com.drps.ams.dto.ApiResponseEntity;

public interface ApartmentDetailsService {

	ApiResponseEntity get() throws Exception;
	ApiResponseEntity getById(Long id) throws Exception;

}
