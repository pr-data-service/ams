package com.drps.ams.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ExpenseHeadDTO;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.entity.ExpenseHeadEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.repository.ExpenseHeadRepository;
import com.drps.ams.service.CommonService;
import com.drps.ams.service.ExpenseHeadService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DynamicQuery;
import com.drps.ams.util.Utils;

import lombok.NonNull;

@Service
public class ExpenseHeadServiceImpl implements ExpenseHeadService {

	@Autowired
	ExpenseHeadRepository expenseHeadRepository;
	
	@Autowired
	CommonService commonService;
	
	@Override
	public ApiResponseEntity saveOrUpdate(@NonNull ExpenseHeadDTO expenseHeadDTO) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(isDuplicateRecord(expenseHeadDTO)) {
			throw new DuplicateRecordException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
		}
		
		ExpenseHeadEntity expenseHeadEntity = null;
		if(expenseHeadDTO.getId() != null && expenseHeadDTO.getId() > 0) {
			expenseHeadEntity = expenseHeadRepository.findById(expenseHeadDTO.getId()).get();
			BeanUtils.copyProperties(expenseHeadDTO, expenseHeadEntity, Utils.getIgnoreEntityPropsOnUpdate(null));
			expenseHeadEntity.setModifiedBy(userContext.getUserDetailsEntity().getId());
		} else {
			expenseHeadEntity = new ExpenseHeadEntity();
			BeanUtils.copyProperties(expenseHeadDTO, expenseHeadEntity);
			expenseHeadEntity.setSessionId(userContext.getSessionId());
			expenseHeadEntity.setApartmentId(userContext.getApartmentId());
			expenseHeadEntity.setCreatedBy(userContext.getUserDetailsEntity().getId());
			expenseHeadEntity.setModifiedBy(userContext.getUserDetailsEntity().getId());
		}
		
			
		expenseHeadRepository.save(expenseHeadEntity);
			
		BeanUtils.copyProperties(expenseHeadEntity, expenseHeadDTO);	
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, expenseHeadDTO);
	}

	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		List<ExpenseHeadEntity> list = expenseHeadRepository.findAll(new DynamicQuery<ExpenseHeadEntity>(reqParamDto));
		
		List<ExpenseHeadDTO> rtnList = Utils.convertList(list, ExpenseHeadDTO.class);
		commonService.addUserDetailsToDTO(rtnList, ExpenseHeadDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public ApiResponseEntity get() {
		UserContext userContext = Utils.getUserContext();
		
		List<ExpenseHeadEntity> list = expenseHeadRepository.getAll(userContext.getApartmentId(), userContext.getSessionId());
		List<ExpenseHeadDTO> rtnList = Utils.convertList(list, ExpenseHeadDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}

	@Override
	public ApiResponseEntity getById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			ExpenseHeadEntity expenseHeadEntity = expenseHeadRepository.findById(id).get();
			if(expenseHeadEntity != null) {
				ExpenseHeadDTO expenseHeadDTO = new ExpenseHeadDTO();
				BeanUtils.copyProperties(expenseHeadEntity, expenseHeadDTO);
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, expenseHeadDTO);
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
			expenseHeadRepository.deleteById(id);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity deleteAllById(List<Long> ids) throws Exception {
		UserContext userContext = Utils.getUserContext();
			
		if(ids != null && ids.size() > 0) {
			expenseHeadRepository.deleteAllById(ids);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	private boolean isDuplicateRecord(ExpenseHeadDTO expenseHeadDTO) {
		UserContext userContext = Utils.getUserContext();
		
		List<ExpenseHeadEntity> list = expenseHeadRepository.findByTitle(userContext.getApartmentId(), userContext.getSessionId(), expenseHeadDTO.getTitle());
		if(list != null && expenseHeadDTO.getId() != null && expenseHeadDTO.getId() > 0) {
			list = list.stream().filter( f -> f.getId() != expenseHeadDTO.getId()).toList();
		}
		
		return list != null && list.size() > 0 ? true : false;
	}
}
