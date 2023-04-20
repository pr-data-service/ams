package com.drps.ams.service;

import java.util.List;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EmailServiceDTO;

public interface EmailSignatureService {
	
	ApiResponseEntity getByApartmentId();

	ApiResponseEntity saveOrUpdate(EmailServiceDTO dto) throws Exception;
}