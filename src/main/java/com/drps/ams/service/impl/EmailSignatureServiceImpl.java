package com.drps.ams.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EmailServiceDTO;
import com.drps.ams.entity.EmailServiceEntity;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.repository.EmailServiceRepository;
import com.drps.ams.service.EmailSignatureService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.Utils;

@Service
public class EmailSignatureServiceImpl implements EmailSignatureService {
	@Autowired
	EmailServiceRepository emailServiceRepository;
	
	@Override
	public ApiResponseEntity saveOrUpdate(EmailServiceDTO dto) throws Exception {
		UserContext userContext = Utils.getUserContext();
			if(dto.getId() != null && dto.getId() > 0) {	
				EmailServiceEntity entity = emailServiceRepository.getById(dto.getId());
				if (entity != null) {
					entity.setModifiedBy(userContext.getUserId());
					entity.setIsActive(dto.getIsActive());
					entity = emailServiceRepository.save(entity);
					dto.setId(entity.getId());
					dto.setIsActive(entity.getIsActive());
					dto.setType(entity.getType());
				}
				else {
					throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
				}
				
			}else {	
				EmailServiceEntity entity = new EmailServiceEntity();
				BeanUtils.copyProperties(dto, entity);
				entity.setApartmentId(userContext.getApartmentId());
				entity.setIsActive(dto.getIsActive());
				entity.setCreatedBy(userContext.getUserId());
				entity = emailServiceRepository.save(entity);
				dto.setId(entity.getId());
				dto.setIsActive(entity.getIsActive());
				dto.setType(entity.getType());
			}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dto) ;
	}
	
	
	@Override
	public ApiResponseEntity getByApartmentId() {
		UserContext userContext = Utils.getUserContext();
	
		List<EmailServiceEntity> list = emailServiceRepository.getByApartmentId(userContext.getApartmentId());
		List<EmailServiceDTO> dtos = new ArrayList<EmailServiceDTO>();
		for(EmailServiceEntity entity: list) {
			EmailServiceDTO dto = new EmailServiceDTO();
			dto.setId(entity.getId());
			dto.setIsActive(entity.getIsActive());
			dto.setType(entity.getType());
			dtos.add(dto);
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dtos);
	}
}