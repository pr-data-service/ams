/**
 * 
 */
package com.drps.ams.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EventsDTO;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.dto.SessionDetailsDTO;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.MaintenanceEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.exception.UserContextNotFoundException;
import com.drps.ams.repository.ExpensesRepository;
import com.drps.ams.repository.FlatDetailsRepository;
import com.drps.ams.repository.MaintenanceRepository;
import com.drps.ams.repository.PaymentRepository;
import com.drps.ams.repository.SessionDetailsRepository;
import com.drps.ams.service.CommonService;
import com.drps.ams.service.ExpensesService;
import com.drps.ams.service.PaymentService;
import com.drps.ams.service.SessionDetailsService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DateUtils;
import com.drps.ams.util.Utils;

/**
 * @author 002ZX2744
 *
 */
@Service
public class SessionDetailsServiceImpl implements SessionDetailsService {

	@Autowired
	SessionDetailsRepository sessionDetailsRepository;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	private JwtUserDetailsServiceImpl jwtUserDetailsService;
	
	@Autowired
	MaintenanceRepository maintenanceRepository;
	
	@Autowired
	PaymentRepository paymentRepository;
	
	@Autowired
	ExpensesRepository expensesRepository;
	
	@Autowired
	FlatDetailsRepository flatDetailsRepository;
	
	@Override
	public ApiResponseEntity saveOrUpdate(@NonNull SessionDetailsDTO dto) {
		
		UserContext userContext = Utils.getUserContext();
		dto = saveOrUpdate(dto, userContext.getApartmentId(), userContext.getUserId());
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dto);
	}
	
	@Override
	public SessionDetailsDTO saveOrUpdate(@NonNull SessionDetailsDTO dto, Long apartmentId, Long userId) {
		
		if(isDuplicateRecord(dto, apartmentId)) {
			throw new DuplicateRecordException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
		} else if(!isValidSessionByDate(dto, apartmentId)) {
			throw new RuntimeException("Invalid date range. Please check date.");
		}
		
		SessionDetailsEntity entity = null;
		if(dto.getId() != null && dto.getId() > 0) {
			entity = sessionDetailsRepository.findById(dto.getId()).get();
			if(entity == null) {
				throw new NoRecordFoundException("Record not found");
			} else {
				BeanUtils.copyProperties(dto, entity, Utils.getIgnoreEntityPropsOnUpdate(new String[] {"id"}));
				entity.setModifiedBy(userId);
			}
			
		} else {
			entity = new SessionDetailsEntity();
			BeanUtils.copyProperties(dto, entity);
			entity.setApartmentId(apartmentId);
			entity.setCreatedBy(userId);
			entity.setModifiedBy(userId);
		}
		sessionDetailsRepository.save(entity);
		
		BeanUtils.copyProperties(entity, dto);	
		
		return dto;
	}
	
	
	@Override
	public List<SessionDetailsDTO> getSessionList(String username) {
				
		UserContext userContext = null;
		if(username != null) {
			userContext = jwtUserDetailsService.loadUserByUsername(username);
		}
		
		if(userContext == null) {
			throw new UserContextNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_USER_CONTEXT_NOT_FOUND_EXCEPTION));
		}
		
		List<SessionDetailsEntity> list = sessionDetailsRepository.findByApartmentId(userContext.getApartmentId());
		List<SessionDetailsDTO> rtnList = Utils.convertList(list, SessionDetailsDTO.class);
		return rtnList;
	}
	
	@Override
	public ApiResponseEntity get() {
		UserContext userContext = Utils.getUserContext();
		
		List<SessionDetailsEntity> list = sessionDetailsRepository.getAll(userContext.getApartmentId());
		List<SessionDetailsDTO> rtnList = Utils.convertList(list, SessionDetailsDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public void addSessionIdAndMaintenanceOnList(List<PaymentDetailsDTO> list, Long flatId) {
		UserContext userContext = Utils.getUserContext();
		
		if(list != null && !list.isEmpty()) {
			String flatType = flatDetailsRepository.getFlatType(userContext.getApartmentId(), flatId);
			List<SessionDetailsEntity> sessionList = sessionDetailsRepository.getAll(userContext.getApartmentId());
			List<MaintenanceEntity> maintenanceList = maintenanceRepository.getAllActiveMaintenanceForFlat(userContext.getApartmentId(), flatId);
			
			
//			LocalDate mntDt = null;
//			LocalDate frmDt = null;
//			LocalDate toDt = null;
			for(PaymentDetailsDTO dto : list) {
				LocalDate mntDt = LocalDate.of(dto.getPaymentYear(), dto.getPaymentMonth(), 1);
				
				SessionDetailsEntity sessionDetailsEntity = sessionList.stream().filter( f -> {
					LocalDate frmDt = new java.sql.Date(f.getFromDate().getTime()).toLocalDate();
					LocalDate toDt = new java.sql.Date(f.getToDate().getTime()).toLocalDate();
					
					return mntDt.isEqual(frmDt) || ( mntDt.isAfter(frmDt) && mntDt.isBefore(toDt) ) ? true : false;
				}).findAny().orElse(null);
				
				if(sessionDetailsEntity != null) {
					dto.setPaymentForSessionId(sessionDetailsEntity.getId());
					dto.setPaymentForSessionName(sessionDetailsEntity.getName());
					
					
					//Session wise Maintenance
					MaintenanceEntity mainEnt = maintenanceList.stream()
							.filter(f -> f.getSessionId() != null
									&& f.getSessionId() == sessionDetailsEntity.getId()).findFirst().orElse(null);
					
					//If Session wise Maintenance not found then Maintenance will get from previous Session
					if(mainEnt == null) {
												
						SessionDetailsEntity prevSessionDetailsEntity = sessionList.stream().filter( f -> {
							LocalDate curntFrmDt = new java.sql.Date(sessionDetailsEntity.getFromDate().getTime()).toLocalDate();
							curntFrmDt = curntFrmDt.minusDays(1);
							LocalDate frmDt = new java.sql.Date(f.getFromDate().getTime()).toLocalDate();
							LocalDate toDt = new java.sql.Date(f.getToDate().getTime()).toLocalDate();
							
							return curntFrmDt.isEqual(toDt) || ( curntFrmDt.isAfter(frmDt) && curntFrmDt.isBefore(toDt) ) ? true : false;
						}).findAny().orElse(null);
						
						if(prevSessionDetailsEntity != null) {
							mainEnt = maintenanceList.stream()
									.filter(f -> f.getSessionId() != null
											&& f.getSessionId() == prevSessionDetailsEntity.getId()).findFirst().orElse(null);
							
							
							if(mainEnt != null) {
								MaintenanceEntity newMainEnt = new MaintenanceEntity();
								BeanUtils.copyProperties(mainEnt, newMainEnt, "id");
								newMainEnt.setSessionId(userContext.getSessionId());
								newMainEnt.setLastActiveMaintId(Long.valueOf(0));
								newMainEnt.setCreatedBy(ApiConstants.SYSTEM_USER_ID);
								newMainEnt.setModifiedBy(ApiConstants.SYSTEM_USER_ID);
								newMainEnt.setCreatedDate(new Date());
								newMainEnt.setModifiedDate(new Date());
								maintenanceRepository.save(newMainEnt);
								maintenanceList.add(newMainEnt);
							}
						}						
					}
					
					
					//If Session wise Maintenance not found then Maintenance will get from current Session.
//					if(mainEnt == null) {
//						mainEnt = maintenanceList.stream()
//								.filter(f -> f.getSessionId() != null
//										&& f.getSessionId() == userContext.getSessionId()).findFirst().orElse(null);
//					}
					dto.setAmount(Double.valueOf(0));
					if(mainEnt != null) {
						if(ApiConstants.FLAT_TYPE_DOUBLE.equals(flatType)) {
							dto.setAmount(mainEnt.getAmount()*2);
						} else {
							dto.setAmount(mainEnt.getAmount());
						}
					}
				} else {
					throw new RuntimeException("Session not found.");
				}
			}
		}
	}
	
	@Override
	public void addPaymentForSessionNameOnList(List<PaymentDetailsDTO> list) {
		UserContext userContext = Utils.getUserContext();
		
		if(list != null && !list.isEmpty()) {
			List<SessionDetailsEntity> sessionList = sessionDetailsRepository.getAll(userContext.getApartmentId());
			
			for(PaymentDetailsDTO dto : list) {
				SessionDetailsEntity sessionDetailsEntity = sessionList.stream().filter( f ->  f.getId().equals(dto.getPaymentForSessionId())).findAny().orElse(null);
				if(sessionDetailsEntity != null) {
					dto.setPaymentForSessionName(sessionDetailsEntity.getName());
				}
			}
		}
	}

	private boolean isDuplicateRecord(SessionDetailsDTO dto, Long apartmentId) {
		
		List<SessionDetailsEntity> list = sessionDetailsRepository.findByName(apartmentId, dto.getName());
		if(list != null && dto.getId() != null && dto.getId() > 0) {
			list = list.stream().filter( f -> f.getId() != dto.getId()).collect(Collectors.toList());
		}
		
		return list != null && list.size() > 0 ? true : false;
	}
	
	private boolean isValidSessionByDate(SessionDetailsDTO dto, Long apartmentId) {
		
		String strDt = DateUtils.dateToString(dto.getToDate()) + " 11:59:59";
		Date dtoToDt = DateUtils.stringToDateTime(strDt);
		dto.setToDate(dtoToDt);
		
		SessionDetailsEntity lastSession = null;
		Date lastSessionToDt = null;
		List<SessionDetailsEntity> list = sessionDetailsRepository.findSessionOrderByToDate(apartmentId);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			lastSession = list.get(0);
			String strDate = DateUtils.dateToString(lastSession.getToDate()) + " 11:59:59";
			lastSessionToDt = DateUtils.stringToDateTime(strDate);
		}
		
		
		
		if((lastSessionToDt != null && (lastSessionToDt.getTime() > dto.getFromDate().getTime()
				|| lastSessionToDt.getTime() > dto.getToDate().getTime()))
				|| ( dto.getFromDate().getTime() > dto.getToDate().getTime())) {
			
			return false;
			
		}
		
		return true;
	}
	
	@Override
	public ApiResponseEntity deleteById(Long id) {
		UserContext userContext = Utils.getUserContext();
			
		if(id != null && id > 0) {
			if(sessionDetailsRepository.getAll(userContext.getApartmentId()).size() == 1) {
				throw new RuntimeException("Apartment has only one Session. Add more and tyr again...!");
			}
			
			if(isUsedSession(id)) {
				throw new RuntimeException("Session already used.");
			}
			SessionDetailsEntity entity = sessionDetailsRepository.findById(id).get();
			if(userContext.getSessionId() == id.longValue()) {
				new RuntimeException();
			}
			sessionDetailsRepository.deleteById(id);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	public boolean isUsedSession(Long sessionId) {
		UserContext userContext = Utils.getUserContext();
		List list = maintenanceRepository.getAll(userContext.getApartmentId(), sessionId, PageRequest.of(0,1));
		if(list == null || list.isEmpty()) {
			list = paymentRepository.getAll(userContext.getApartmentId(), sessionId, PageRequest.of(0,1));
		} 
		if(list == null || list.isEmpty()) {
			list = expensesRepository.getAll(userContext.getApartmentId(), sessionId, PageRequest.of(0,1));
		}
		
		if(list == null || list.isEmpty()) {
			return false;
		}
		
		return true;
	}
}
