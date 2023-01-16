/**
 * 
 */
package com.drps.ams.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dto.ApartmentDetailsDTO;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.PaymentDTO;
import com.drps.ams.entity.ApartmentDetailsEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.PaymentEntity;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.repository.ApartmentDetailsRepository;
import com.drps.ams.service.ApartmentDetailsService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.Utils;

/**
 * @author 002ZX2744
 *
 */
@Service
public class ApartmentDetailsServiceImpl implements ApartmentDetailsService {

	private static final Logger logger = LogManager.getLogger(ApartmentDetailsServiceImpl.class);
	
	@Autowired
	ApartmentDetailsRepository apartmentDetailsRepository;
	
	@Override
	public ApiResponseEntity get() throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		ApartmentDetailsEntity entity = apartmentDetailsRepository.getById(userContext.getApartmentId());
		if(entity != null) {
			ApartmentDetailsDTO dto = new ApartmentDetailsDTO();
			BeanUtils.copyProperties(entity, dto);
			
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dto);
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity getById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			ApartmentDetailsEntity entity = apartmentDetailsRepository.getById(id);
			if(entity != null) {
				ApartmentDetailsDTO dto = new ApartmentDetailsDTO();
				BeanUtils.copyProperties(entity, dto);
				
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dto);
			} else {
				throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
		
		
	}
}
