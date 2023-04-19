package com.drps.ams.service.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dbquery.DBQueryExecuter;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ExpenseItemsDTO;
import com.drps.ams.dto.MaintenanceDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.dto.UserDetailsDTO;
import com.drps.ams.entity.ExpenseItemsEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.MaintenanceEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.repository.FlatDetailsRepository;
import com.drps.ams.repository.MaintenanceRepository;
import com.drps.ams.repository.PaymentDetailsRepository;
import com.drps.ams.service.CommonService;
import com.drps.ams.service.MaintenanceService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DateUtils;
import com.drps.ams.util.DynamicQuery;
import com.drps.ams.util.Utils;

import lombok.NonNull;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

	@Autowired
	MaintenanceRepository maintenanceRepository;
	
	@Autowired
	FlatDetailsRepository flatDetailsRepository;
	
	@Autowired
	CommonService commonService; 
	
	@Autowired
	DBQueryExecuter dbQueryExecuter;
	
	@Autowired
	PaymentDetailsRepository paymentDetailsRepository;
	
	@Override
	public ApiResponseEntity saveOrUpdate(@NonNull MaintenanceDTO maintenanceDTO) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		
//		if(isDuplicateRecord(maintenanceDTO)) {
//			throw new DuplicateRecordException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
//		}
		
		
		MaintenanceEntity maintenanceEntity = null;
		if(maintenanceDTO.getId() != null && maintenanceDTO.getId() > 0) {
			maintenanceEntity = maintenanceRepository.findById(maintenanceDTO.getId()).get();
			
			if(!maintenanceEntity.getIsActive()) {
				throw new RuntimeException("Maintenance is not active. You can not edit this maintenance.");
			}
			
			if(isMaintenanceUsedAlready(maintenanceEntity)) {
				throw new RuntimeException("Maintenance already used. You can not edit this maintenance.");
			}
			
			BeanUtils.copyProperties(maintenanceDTO, maintenanceEntity, Utils.getIgnoreEntityPropsOnUpdate(null));
			maintenanceEntity.setModifiedBy(userContext.getUserId());
		} else {
						
			maintenanceEntity = new MaintenanceEntity();
			BeanUtils.copyProperties(maintenanceDTO, maintenanceEntity);
			maintenanceEntity.setLastActiveMaintId(Long.valueOf(0));
			maintenanceEntity.setIsActive(true);
			maintenanceEntity.setCreatedBy(userContext.getUserId());
			maintenanceEntity.setModifiedBy(userContext.getUserId());
			maintenanceEntity.setSessionId(userContext.getSessionId());
			maintenanceEntity.setApartmentId(userContext.getApartmentId());
			
			
			List<MaintenanceEntity> list = maintenanceRepository.getAll(userContext.getApartmentId(), userContext.getSessionId(), maintenanceDTO.getFlatId());
			list = list.stream().filter( f -> f.isActive == true).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(list)) {
				
				MaintenanceEntity activeMaint = list.get(0);
				if(activeMaint != null) {
					maintenanceEntity.setLastActiveMaintId(activeMaint.getId());
				}
				
				list.forEach(f -> f.setIsActive(false));
				maintenanceRepository.saveAll(list);
			}
		}
					
		maintenanceRepository.save(maintenanceEntity);
			
		BeanUtils.copyProperties(maintenanceEntity, maintenanceDTO);	
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, maintenanceDTO);
	}

	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		List<MaintenanceDTO> rtnList = dbQueryExecuter.executeQuery(new QueryMaker<MaintenanceDTO>(reqParamDto, MaintenanceDTO.class));
		commonService.addUserDetailsToDTO(rtnList, MaintenanceDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}

	@Override
	public ApiResponseEntity get() {
		UserContext userContext = Utils.getUserContext();
		
		List<MaintenanceEntity> list = maintenanceRepository.getAll(userContext.getApartmentId(), userContext.getSessionId());

		List<MaintenanceDTO> rtnList = Utils.convertList(list, MaintenanceDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}

	@Override
	public ApiResponseEntity getById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			MaintenanceEntity maintenanceEntity = maintenanceRepository.findById(id).get();
			if(maintenanceEntity != null) {
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, Utils.copyProps(maintenanceEntity, MaintenanceDTO.class, false));
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
			MaintenanceEntity maintenanceEntity = maintenanceRepository.findById(id).get();
			if(isMaintenanceUsedAlready(maintenanceEntity)) {
				throw new RuntimeException("Maintenance already used. You can not delete this maintenance.");
			} else {
				deleteMaintenance(maintenanceEntity);
			}
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}

	@Override
	public ApiResponseEntity deleteAllById(List<Long> ids) throws Exception {
		UserContext userContext = Utils.getUserContext();
			
		if(ids != null && ids.size() > 0) {
			if(ids.size() <= 5) {
				ids = ids.stream().sorted().collect(Collectors.toList());
				
				List<MaintenanceEntity> maintenanceEntityList = maintenanceRepository.findAllById(ids);
				for(MaintenanceEntity maintenanceEntity : maintenanceEntityList) {
					if(isMaintenanceUsedAlready(maintenanceEntity)) {
						throw new RuntimeException("Maintenance already used. You can not delete this maintenance.");
					} else {
						deleteMaintenance(maintenanceEntity);
					}
				}				
			} else {
				throw new RuntimeException("More than 5 Maintenance can not delete at a time.");
			}
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	@Transactional
	public void deleteMaintenance(MaintenanceEntity entity) {
		Assert.notNull(entity, "Entity must not null.");
		
		MaintenanceEntity prevMaintenanceEntity = maintenanceRepository.findById(entity.getLastActiveMaintId()).orElse(null);
		MaintenanceEntity nextMaintenanceEntity = maintenanceRepository.findByLastActiveMaintId(entity.getApartmentId(), entity.getSessionId(), entity.getFlatId(), entity.getLastActiveMaintId());
		
		if(nextMaintenanceEntity != null) {
			if(prevMaintenanceEntity != null) {
				nextMaintenanceEntity.setLastActiveMaintId(prevMaintenanceEntity.getId());
			} else {
				nextMaintenanceEntity.setLastActiveMaintId(Long.valueOf(0));
			}
			maintenanceRepository.save(nextMaintenanceEntity);
		}
		 
		
		if(entity.getIsActive() && prevMaintenanceEntity != null) {
			prevMaintenanceEntity.setIsActive(true);
			maintenanceRepository.save(prevMaintenanceEntity);
		}
		
		maintenanceRepository.delete(entity);
	}
	
	public boolean isMaintenanceUsedAlready(MaintenanceEntity maintenanceEntity) {
		double amount = maintenanceEntity.getAmount();
		
		FlatDetailsEntity flat = flatDetailsRepository.findById(maintenanceEntity.getFlatId()).orElse(null);
		if(flat == null) {
			throw new RuntimeException("Flat Details not found. Failed to check Maintenance!");
		} else if(ApiConstants.FLAT_TYPE_DOUBLE.equalsIgnoreCase(flat.getFlatType())) {
			amount = amount * 2;
		}
		
		List<PaymentDetailsEntity> list = paymentDetailsRepository.getPaymentForSessionId(maintenanceEntity.getApartmentId(), maintenanceEntity.getSessionId(), maintenanceEntity.getFlatId(), amount);
		MaintenanceEntity lastActivePayDtls = maintenanceRepository.getAll(maintenanceEntity.getApartmentId(), maintenanceEntity.getSessionId(), maintenanceEntity.getFlatId(), maintenanceEntity.getId()).stream().findFirst().orElse(null);
		
		
		int startYear = maintenanceEntity.getCreatedDate().getYear()+1900;
		int startMonth = maintenanceEntity.getCreatedDate().getMonth()+1;
		System.out.println(String.format("Start Date: %d-%d", startMonth, startYear));
		
		Date endDt = lastActivePayDtls == null ? new Date() : lastActivePayDtls.getCreatedDate();
		int endYear = endDt.getYear()+1900;
		int endMonth = endDt.getMonth()+1;
		System.out.println(String.format("End Date: %d-%d", endMonth, endYear));
		
		list = list.stream().filter(f -> {
			
			System.out.println(String.format("Maintenance month: %d & year: %d", f.getPaymentMonth(), f.getPaymentYear()));
			
			if(f.getPaymentYear() >= startYear && f.getPaymentYear() <= endYear) {
				if(f.getPaymentYear() > startYear && f.getPaymentYear() < endYear) {
					return true;
				}
				else if(f.getPaymentYear() == startYear && f.getPaymentYear() < endYear) {
					if(f.getPaymentMonth() >= startMonth) {
						return true;
					}
				}
				else if(f.getPaymentYear() > startYear && f.getPaymentYear() == endYear) {
					if(f.getPaymentMonth() <= endMonth) {
						return true;
					}
				}
				else if(f.getPaymentYear() == startYear && f.getPaymentYear() == endYear) {
					if(f.getPaymentMonth() >= startMonth && f.getPaymentMonth() <= endMonth) {
						return true;
					}
				}				
			}
			
			return false;
		}).collect(Collectors.toList());
		
		return list != null && list.size() > 0 ? true : false;
	}

}
