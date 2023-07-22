package com.drps.ams.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dbquery.DBQueryExecuter;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EventsDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.repository.EventsRepository;
import com.drps.ams.repository.ExpensesRepository;
import com.drps.ams.repository.PaymentDetailsRepository;
import com.drps.ams.service.CommonService;
import com.drps.ams.service.EventsService;
import com.drps.ams.util.Utils;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DynamicQuery;

import lombok.NonNull;

@Service
public class EventsServiceImpl implements EventsService {

	private static final  Logger logger = LogManager.getLogger(EventsServiceImpl.class);
	
	
	@Autowired
	EventsRepository eventsRepository;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	ExpensesRepository expensesRepository;
	
	@Autowired
	DBQueryExecuter dbQueryExecuter;
	
	@Autowired
	PaymentDetailsRepository paymentDetailsRepository;
	
	@Override
	public ApiResponseEntity saveOrUpdate(@NonNull EventsDTO eventsDTO) throws Exception {
		
		UserContext userContext = Utils.getUserContext();
		
		if(ApiConstants.DEFAULT_EVENT_NAME.equalsIgnoreCase(eventsDTO.getName())) {
				throw new RuntimeException("This event name used by system. Please try with another name.");
		} else if(isDuplicateRecord(eventsDTO)) {
			throw new DuplicateRecordException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
		}
		
		EventsEntity eventsEntity = null;
		if(eventsDTO.getId() != null && eventsDTO.getId() > 0) {
			eventsEntity = eventsRepository.findById(eventsDTO.getId()).get();
			if(eventsEntity == null) {
				throw new NoRecordFoundException("Record not found");
			} else if(ApiConstants.DEFAULT_EVENT_NAME.equals(eventsEntity.getName())) {
				throw new RuntimeException("This system record can not update or modify.");
			} else {
				BeanUtils.copyProperties(eventsDTO, eventsEntity, Utils.getIgnoreEntityPropsOnUpdate(new String[] {"isActive"}));
				eventsEntity.setModifiedBy(userContext.getUserId());
			}
			
		} else {
			eventsEntity = new EventsEntity();
			BeanUtils.copyProperties(eventsDTO, eventsEntity);
			eventsEntity.setApartmentId(userContext.getApartmentId());
			eventsEntity.setCreatedBy(userContext.getUserId());
			eventsEntity.setModifiedBy(userContext.getUserId());
		}
		
			
		eventsRepository.save(eventsEntity);
			
		BeanUtils.copyProperties(eventsEntity, eventsDTO);	
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, eventsDTO);
	}

	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		
		UserContext userContext = Utils.getUserContext();
		
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		List<EventsDTO> rtnList = dbQueryExecuter.executeQuery(new QueryMaker<EventsDTO>(reqParamDto, EventsDTO.class));
		rtnList = rtnList.stream().filter( f -> !ApiConstants.DEFAULT_EVENT_NAME.equalsIgnoreCase(f.getName())).collect(Collectors.toList());
		commonService.addUserDetailsToDTO(rtnList, EventsDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public ApiResponseEntity get(String reqParams) {
		
		UserContext userContext = Utils.getUserContext();
		
		//List<EventsEntity> list = eventsRepository.getAll(userContext.getApartmentId(), userContext.getSessionId());
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		List<EventsEntity> list = eventsRepository.findAll(new DynamicQuery<EventsEntity>(reqParamDto));
		list = list.stream().filter( f -> !ApiConstants.DEFAULT_EVENT_NAME.equalsIgnoreCase(f.getName())).collect(Collectors.toList());
		List<EventsDTO> rtnList = Utils.convertList(list, EventsDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}

	@Override
	public ApiResponseEntity getById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			EventsEntity eventsEntity = eventsRepository.findById(id).get();
			if(eventsEntity != null) {
				EventsDTO eventsDTO = new EventsDTO();
				BeanUtils.copyProperties(eventsEntity, eventsDTO);
				commonService.addUserDetailsToDTO(eventsDTO, EventsDTO.class);
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, eventsDTO);
			} else {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	private boolean isDuplicateRecord(EventsDTO eventsDTO) {
		UserContext userContext = Utils.getUserContext();
		
		List<EventsEntity> list = eventsRepository.findByName(userContext.getApartmentId(), eventsDTO.getName());
		if(list != null && eventsDTO.getId() != null && eventsDTO.getId() > 0) {
			list = list.stream().filter( f -> f.getId() != eventsDTO.getId()).collect(Collectors.toList());
		}
		
		return list != null && list.size() > 0 ? true : false;
	}
	
	@Transactional
	@Override
	public ApiResponseEntity activeInActive(Long id, boolean isActive) throws Exception {
		UserContext userContext = Utils.getUserContext();

		if(id != null && id > 0) {			
			EventsEntity eventsEntity = eventsRepository.findById(id).get();
			if(eventsEntity != null) {
				eventsEntity.setIsActive(isActive);
				eventsRepository.save(eventsEntity);
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
			} else {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	public boolean isEventUsed (Long eventId) {
		
		boolean isExistInPaymentDetails = paymentDetailsRepository.isEventExist(eventId);
		boolean isExistInExpenseItems = expensesRepository.isEventExist(eventId);
		return (isExistInExpenseItems || isExistInPaymentDetails);
	}
	
	
	@Override
	public ApiResponseEntity deleteAllById(List<Long> ids) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(ids != null && ids.size() > 0) {
			ids = ids.stream().filter( f -> !isEventUsed(f)).collect(Collectors.toList());
			eventsRepository.deleteAllById(ids);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS),ids);
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity getByExpenseId(Long expenseId) throws Exception {
		UserContext userContext = Utils.getUserContext();
		if(expenseId != null && expenseId > 0) {
			ExpensesEntity expEntity = expensesRepository.findById(expenseId).orElse(null);
			Long eventId = expEntity != null ? expEntity.getEventId() : 0;
			if(eventId != null && eventId > 0) {
				EventsEntity eventsEntity = eventsRepository.findById(eventId).get();
				if(eventsEntity != null) {
					EventsDTO eventsDto = new EventsDTO();
					BeanUtils.copyProperties(eventsEntity, eventsDto);
					return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, eventsDto);
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
