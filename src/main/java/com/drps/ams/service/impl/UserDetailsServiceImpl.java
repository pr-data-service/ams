package com.drps.ams.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dbquery.DBQueryExecuter;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ExpenseItemsDTO;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.dto.UserDetailsDTO;
import com.drps.ams.entity.ExpenseItemsEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.exception.UserContextNotFoundException;
import com.drps.ams.repository.LinkFlatDetailsAndUserDetailsRepository;
import com.drps.ams.repository.UserDetailsRepository;
import com.drps.ams.service.UserDetailsService;
import com.drps.ams.util.Utils;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DynamicQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UserDetailsRepository userDetailsRepository;
	
	@Autowired
	LinkFlatDetailsAndUserDetailsRepository linkFlatDetailsAndUserDetailsRepository;
	
	@Autowired
	DBQueryExecuter dbQueryExecuter;

	@Override
	public ApiResponseEntity saveOrUpdate(@NonNull UserDetailsDTO userDetailsDTO) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		
		if(isDuplicateRecord(userDetailsDTO)) {
			throw new DuplicateRecordException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
		}
		
		
		UserDetailsEntity userDetailsEntity = null;
		if(userDetailsDTO.getId() != null && userDetailsDTO.getId() > 0) {
			userDetailsEntity = userDetailsRepository.findById(userDetailsDTO.getId()).get();
			BeanUtils.copyProperties(userDetailsDTO, userDetailsEntity, Utils.getIgnoreEntityPropsOnUpdate(null));
			userDetailsEntity.setModifiedBy(userContext.getUserDetailsEntity().getId());
		} else {
			userDetailsEntity = new UserDetailsEntity();
			BeanUtils.copyProperties(userDetailsDTO, userDetailsEntity);
			userDetailsEntity.setCreatedBy(userContext.getUserId());
			userDetailsEntity.setModifiedBy(userContext.getUserId());
			userDetailsEntity.setApartmentId(userContext.getApartmentId());
		}
		
			
		userDetailsRepository.save(userDetailsEntity);
			
		BeanUtils.copyProperties(userDetailsEntity, userDetailsDTO);	
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, userDetailsDTO);
	}

	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		List<ExpenseItemsDTO> rtnList = dbQueryExecuter.executeQuery(new QueryMaker<UserDetailsDTO>(reqParamDto, UserDetailsDTO.class));
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}

	@Override
	public ApiResponseEntity getById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			UserDetailsEntity userDetailsEntity = userDetailsRepository.findById(id).get();
			if(userDetailsEntity != null) {
				UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
				BeanUtils.copyProperties(userDetailsEntity, userDetailsDTO);
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, userDetailsDTO);
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
			userDetailsRepository.deleteById(id);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity deleteAllById(List<Long> ids) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(ids != null && ids.size() > 0) {
			userDetailsRepository.deleteAllById(ids);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}

	@Override
	public ApiResponseEntity get() {
		UserContext userContext = Utils.getUserContext();
				
		List<UserDetailsEntity> list = userDetailsRepository.findAll();

		List<UserDetailsDTO> rtnList = new ArrayList<>();
		if (list != null) {
			UserDetailsDTO userDetailsDTO = null;
			for (UserDetailsEntity userDetailsEntity : list) {
				userDetailsDTO = new UserDetailsDTO();
				BeanUtils.copyProperties(userDetailsEntity, userDetailsDTO);
				rtnList.add(userDetailsDTO);
			}
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public boolean isDuplicateRecord(UserDetailsDTO userDetailsDTO) {
		List<UserDetailsEntity> list = userDetailsRepository.findByContactNo1(userDetailsDTO.getContactNo1());
		if(list != null && userDetailsDTO.getId() != null && userDetailsDTO.getId() > 0) {
			list = list.stream().filter( f -> f.getId() != userDetailsDTO.getId()).collect(Collectors.toList());
		}
		
		return list != null && list.size() > 0 ? true : false;
	}
	
	@Override
	public ApiResponseEntity getByFlatId(Long flatId) throws Exception {
		UserContext userContext = Utils.getUserContext();
		if(flatId != null && flatId > 0) {
			Long userId = linkFlatDetailsAndUserDetailsRepository.getActiveLinkUserId(userContext.getApartmentId(), flatId);
		
			if(userId != null && userId > 0) {
				UserDetailsEntity userDetailsEntity = userDetailsRepository.findById(userId).get();
				if(userDetailsEntity != null) {
					UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
					BeanUtils.copyProperties(userDetailsEntity, userDetailsDTO);
					return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, userDetailsDTO);
				} else {
					throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
				}
			} else {
				throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}		
	}
}
