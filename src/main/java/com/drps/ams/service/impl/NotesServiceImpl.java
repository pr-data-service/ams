package com.drps.ams.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dbquery.DBQueryExecuter;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.dto.NotesDTO;
import com.drps.ams.dto.PaymentDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.NotesEntity;
import com.drps.ams.entity.PaymentEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.repository.NotesRepository;
import com.drps.ams.service.CommonService;
import com.drps.ams.service.NotesService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.Utils;

import lombok.NonNull;

@Service
public class NotesServiceImpl implements NotesService {

	private static final  Logger logger = LogManager.getLogger(NotesServiceImpl.class);
	
	@Autowired
	NotesRepository notesRepository;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	DBQueryExecuter dbQueryExecuter;
	
	@Override
	public ApiResponseEntity saveOrUpdate(@NonNull NotesDTO dto) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(ApiConstants.NOTE_TYPE_SYSTEM.equalsIgnoreCase(dto.getNoteType())) {
			throw new RuntimeException("System note can not save manually.");
		}
		
		if(isDuplicateRecord(dto)) {
			throw new DuplicateRecordException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
		}
		
		NotesEntity entity = null;
		if(dto.getId() != null && dto.getId() > 0) {
			entity = notesRepository.findById(dto.getId()).get();
			BeanUtils.copyProperties(dto, entity, Utils.getIgnoreEntityPropsOnUpdate(null));
			entity.setModifiedBy(userContext.getUserDetailsEntity().getId());
		} else {
			entity = new NotesEntity();
			BeanUtils.copyProperties(dto, entity);
			entity.setSessionId(userContext.getSessionId());
			entity.setApartmentId(userContext.getApartmentId());
			entity.setCreatedBy(userContext.getUserDetailsEntity().getId());
			entity.setModifiedBy(userContext.getUserDetailsEntity().getId());
		}
		
			
		notesRepository.save(entity);
			
		BeanUtils.copyProperties(entity, dto);	
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dto);
	}
	
	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		List<NotesDTO> rtnList = dbQueryExecuter.executeQuery(new QueryMaker<NotesDTO>(reqParamDto, NotesDTO.class));
		commonService.addUserDetailsToDTO(rtnList, NotesDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public ApiResponseEntity get(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			NotesEntity entity = notesRepository.getById(id);
			if(entity != null) {
				NotesDTO dto = new NotesDTO();
				BeanUtils.copyProperties(entity, dto);
				
				commonService.addUserDetailsToDTO(dto, NotesDTO.class);
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dto);
			} else {
				throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity deleteById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
			
		if(id != null && id > 0) {
			notesRepository.deleteById(id);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	private boolean isDuplicateRecord(NotesDTO dto) {
		UserContext userContext = Utils.getUserContext();
		
		List<NotesEntity> list = null;
		if(ApiConstants.NOTE_TYPE_SYSTEM.equalsIgnoreCase(dto.getNoteType())) {
			list = notesRepository.findByNoteType(userContext.getApartmentId(), userContext.getSessionId(), dto.getParentObject(), dto.getParentRecordId(), ApiConstants.NOTE_TYPE_SYSTEM);
		}
		if(list != null && dto.getId() != null && dto.getId() > 0) {
			list = list.stream().filter( f -> f.getId() != dto.getId()).collect(Collectors.toList());
		}
		
		return list != null && list.size() > 0 ? true : false;
	}
	
	@Override
	public void createSystemNote(String parentObject, Long parentRecordId, String title, String noteText) {
		UserContext userContext = Utils.getUserContext();
		
		if(!StringUtils.isEmpty(parentObject) && parentRecordId != null && parentRecordId > 0 
				&& !StringUtils.isEmpty(title) && !StringUtils.isEmpty(noteText)) {
			
			NotesEntity noteEntity = new NotesEntity();
			noteEntity.setNoteType(ApiConstants.NOTE_TYPE_SYSTEM);
			noteEntity.setTitle(title);
			noteEntity.setNoteText(noteText);
			noteEntity.setParentObject(parentObject);
			noteEntity.setParentRecordId(parentRecordId);
			noteEntity.setSessionId(userContext.getSessionId());
			noteEntity.setApartmentId(userContext.getApartmentId());
			noteEntity.setCreatedBy(ApiConstants.SYSTEM_USER_ID);
			noteEntity.setModifiedBy(ApiConstants.SYSTEM_USER_ID);
			notesRepository.save(noteEntity);
		}
		
	}
}
