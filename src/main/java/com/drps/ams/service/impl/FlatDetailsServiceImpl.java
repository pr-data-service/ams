package com.drps.ams.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dbquery.DBQueryExecuter;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ExpenseItemsDTO;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.dto.MaintenanceDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.dto.UserDetailsDTO;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.entity.ExpenseItemsEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.exception.UserContextNotFoundException;
import com.drps.ams.repository.FlatDetailsRepository;
import com.drps.ams.service.CommonService;
import com.drps.ams.service.FlatDetailsService;
import com.drps.ams.util.Utils;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DynamicQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;

@Service
public class FlatDetailsServiceImpl implements FlatDetailsService {

	private static final  Logger logger = LogManager.getLogger(FlatDetailsServiceImpl.class);
	
	@Autowired
	FlatDetailsRepository flatDetailsRepository;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	DBQueryExecuter dbQueryExecuter;
	
	@Override
	public ApiResponseEntity saveOrUpdate(@NonNull FlatDetailsDTO flatDetailsDTO) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(isDuplicateRecord(flatDetailsDTO)) {
			throw new DuplicateRecordException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
		}
		
		FlatDetailsEntity flatDetailsEntity = null;
		if(flatDetailsDTO.getId() != null && flatDetailsDTO.getId() > 0) {
			flatDetailsEntity = flatDetailsRepository.findById(flatDetailsDTO.getId()).get();
			BeanUtils.copyProperties(flatDetailsDTO, flatDetailsEntity, Utils.getIgnoreEntityPropsOnUpdate(null));
			flatDetailsEntity.setModifiedBy(userContext.getUserDetailsEntity().getId());
		} else {
			flatDetailsEntity = new FlatDetailsEntity();
			BeanUtils.copyProperties(flatDetailsDTO, flatDetailsEntity);
			flatDetailsEntity.setApartmentId(userContext.getApartmentId());
			flatDetailsEntity.setCreatedBy(userContext.getUserDetailsEntity().getId());
			flatDetailsEntity.setModifiedBy(userContext.getUserDetailsEntity().getId());
		}
		
			
		flatDetailsRepository.save(flatDetailsEntity);
			
		BeanUtils.copyProperties(flatDetailsEntity, flatDetailsDTO);	
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, flatDetailsDTO);
	}

	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		List<FlatDetailsDTO> rtnList = dbQueryExecuter.executeQuery(new QueryMaker(reqParamDto, FlatDetailsDTO.class));
		commonService.addUserDetailsToDTO(rtnList, FlatDetailsDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public ApiResponseEntity get() {
		UserContext userContext = Utils.getUserContext();
		
		List<FlatDetailsEntity> list = flatDetailsRepository.getAll(userContext.getApartmentId());

		List<FlatDetailsDTO> rtnList = Utils.convertList(list, FlatDetailsDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}

	@Override
	public ApiResponseEntity getById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		
		if(id != null && id > 0) {
			FlatDetailsEntity flatDetailsEntity = flatDetailsRepository.findById(id).get();
			if(flatDetailsEntity != null) {
				FlatDetailsDTO flatDetailsDTO = new FlatDetailsDTO();
				BeanUtils.copyProperties(flatDetailsEntity, flatDetailsDTO);
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, flatDetailsDTO);
			} else {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity deleteById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
			
		if(id != null && id > 0) {
			flatDetailsRepository.deleteById(id);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity deleteAllById(List<Long> ids) throws Exception {
		UserContext userContext = Utils.getUserContext();
			
		if(ids != null && ids.size() > 0) {
			flatDetailsRepository.deleteAllById(ids);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	private boolean isDuplicateRecord(FlatDetailsDTO flatDetailsDTO) {
		UserContext userContext = Utils.getUserContext();
		
		List<FlatDetailsEntity> list = flatDetailsRepository.findByFlatNo(userContext.getApartmentId(), flatDetailsDTO.getFlatNo());
		if(list != null && flatDetailsDTO.getId() != null && flatDetailsDTO.getId() > 0) {
			list = list.stream().filter( f -> f.getId() != flatDetailsDTO.getId()).collect(Collectors.toList());
		}
		
		return list != null && list.size() > 0 ? true : false;
	}
}
