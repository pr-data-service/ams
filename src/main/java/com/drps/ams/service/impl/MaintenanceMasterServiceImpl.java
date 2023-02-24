package com.drps.ams.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.dto.MaintenanceMasterDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.entity.MaintenanceMasterEntity;
import com.drps.ams.entity.MaintenanceMasterEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.repository.FlatDetailsRepository;
import com.drps.ams.repository.MaintenanceMasterRepository;
import com.drps.ams.service.MaintenanceMasterService;
import com.drps.ams.util.Utils;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DynamicQuery;

import lombok.NonNull;

@Service
public class MaintenanceMasterServiceImpl implements MaintenanceMasterService {

	@Autowired
	MaintenanceMasterRepository maintenanceMasterRepository;
	
	
	@Override
	public ApiResponseEntity saveOrUpdate(@NonNull MaintenanceMasterDTO maintenanceMasterDTO) throws Exception {
		
		if(isDuplicateRecord(maintenanceMasterDTO)) {
			throw new DuplicateRecordException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
		}
		
		MaintenanceMasterEntity maintenanceMasterEntity = null;
		if(maintenanceMasterDTO.getId() != null && maintenanceMasterDTO.getId() > 0) {
			maintenanceMasterEntity = maintenanceMasterRepository.findById(maintenanceMasterDTO.getId()).get();
			BeanUtils.copyProperties(maintenanceMasterDTO, maintenanceMasterEntity, new String[]{ "createdBy", "createdDate", "modifiedBy", "modifiedDate"});
		} else {
			maintenanceMasterEntity = new MaintenanceMasterEntity();
			BeanUtils.copyProperties(maintenanceMasterDTO, maintenanceMasterEntity);
		}
		
			
		maintenanceMasterRepository.save(maintenanceMasterEntity);
			
		BeanUtils.copyProperties(maintenanceMasterEntity, maintenanceMasterDTO);	
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, maintenanceMasterDTO);
	}

	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		List<MaintenanceMasterEntity> list = maintenanceMasterRepository.findAll(new DynamicQuery<MaintenanceMasterEntity>(reqParamDto));
		
		List<MaintenanceMasterDTO> rtnList = new ArrayList<>();
		if(list != null) {
			MaintenanceMasterDTO maintenanceMasterDTO = null;
			for(MaintenanceMasterEntity maintenanceMasterEntity : list) {
				maintenanceMasterDTO = new MaintenanceMasterDTO();
				BeanUtils.copyProperties(maintenanceMasterEntity, maintenanceMasterDTO);
				maintenanceMasterDTO.setStrActiveInActive("IN-ACTIVE");
				if(maintenanceMasterDTO.getIsActive() != null && maintenanceMasterDTO.getIsActive()) {
					maintenanceMasterDTO.setStrActiveInActive("ACTIVE");
				}
				rtnList.add(maintenanceMasterDTO);
			}
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public ApiResponseEntity get() {
		List<MaintenanceMasterEntity> list = maintenanceMasterRepository.findAll();

		List<FlatDetailsDTO> rtnList = new ArrayList<>();
		if (list != null) {
			FlatDetailsDTO flatDetailsDTO = null;
			for (MaintenanceMasterEntity flatDetailsEntity : list) {
				flatDetailsDTO = new FlatDetailsDTO();
				BeanUtils.copyProperties(flatDetailsEntity, flatDetailsDTO);
				rtnList.add(flatDetailsDTO);
			}
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}

	@Override
	public ApiResponseEntity getById(Long id) throws Exception {
		if(id != null && id > 0) {
			MaintenanceMasterEntity maintenanceMasterEntity = maintenanceMasterRepository.findById(id).get();
			if(maintenanceMasterEntity != null) {
				MaintenanceMasterDTO maintenanceMasterDTO = new MaintenanceMasterDTO();
				BeanUtils.copyProperties(maintenanceMasterEntity, maintenanceMasterDTO);
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, maintenanceMasterDTO);
			} else {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity deleteById(Long id) throws Exception {
		if(id != null && id > 0) {
			maintenanceMasterRepository.deleteById(id);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity deleteAllById(List<Long> ids) throws Exception {
		if(ids != null && ids.size() > 0) {
			maintenanceMasterRepository.deleteAllById(ids);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Transactional
	@Override
	public ApiResponseEntity activeInActive(Long id) throws Exception {
		if(id != null && id > 0) {			
			MaintenanceMasterEntity maintenanceMasterEntity = maintenanceMasterRepository.findById(id).get();
			if(maintenanceMasterEntity != null) {
				maintenanceMasterRepository.inActiveAll(id);
				maintenanceMasterEntity.setIsActive(true);
				maintenanceMasterRepository.save(maintenanceMasterEntity);
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
			} else {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	private boolean isDuplicateRecord(MaintenanceMasterDTO maintenanceMasterDTO) {
		List<MaintenanceMasterEntity> list = maintenanceMasterRepository.findByAmount(maintenanceMasterDTO.getAmount());
		if(list != null && maintenanceMasterDTO.getId() != null && maintenanceMasterDTO.getId() > 0) {
			list = list.stream().filter( f -> f.getId() != maintenanceMasterDTO.getId()).collect(Collectors.toList());
		}
		
		return list != null && list.size() > 0 ? true : false;
	}
}
