package com.drps.ams.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dbquery.DBQueryExecuter;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ExpensesDTO;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.dto.ListViewFieldDTO;
import com.drps.ams.dto.MaintenanceDTO;
import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
import com.drps.ams.entity.PaymentTypeMasterEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.exception.UserContextNotFoundException;
import com.drps.ams.repository.EventsRepository;
import com.drps.ams.repository.FlatDetailsRepository;
import com.drps.ams.repository.LinkFlatDetailsAndUserDetailsRepository;
import com.drps.ams.repository.MaintenanceMasterRepository;
import com.drps.ams.repository.PaymentDetailsRepository;
import com.drps.ams.repository.PaymentRepository;
import com.drps.ams.repository.PaymentTypeMasterRepository;
import com.drps.ams.repository.SessionDetailsRepository;
import com.drps.ams.service.CommonService;
import com.drps.ams.service.FlatDetailsService;
import com.drps.ams.service.PaymentDetailsService;
import com.drps.ams.service.SessionDetailsService;
import com.drps.ams.util.Utils;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DateUtils;
import com.drps.ams.util.DynamicQuery;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

	private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

	@Autowired
	FlatDetailsService flatDetailsService;
	
	@Autowired
	LinkFlatDetailsAndUserDetailsRepository linkFlatDetailsAndUserDetailsRepository;
	
	@Autowired
	PaymentDetailsRepository paymentDetailsRepository;
	
	@Autowired
	PaymentRepository paymentRepository;
	
	@Autowired
	FlatDetailsRepository flatDetailsRepository;
	
	@Autowired
	MaintenanceMasterRepository maintenanceMasterRepository;
	
	@Autowired
	PaymentTypeMasterRepository paymentTypeMasterRepository;
	
	@Autowired
	EventsRepository eventsRepository;
	
	@Autowired
	SessionDetailsService sessionDetailsService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	SessionDetailsRepository sessionDetailsRepository;
	
	@Autowired
	DBQueryExecuter dbQueryExecuter;
	
	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		
			
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		List<PaymentDetailsDTO> rtnList = dbQueryExecuter.executeQuery(new QueryMaker<PaymentDetailsDTO>(reqParamDto, PaymentDetailsDTO.class));
		
		Comparator<PaymentDetailsDTO> compareByYearThenMonth = Comparator
                .comparing(PaymentDetailsDTO::getPaymentYear, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(PaymentDetailsDTO::getPaymentMonth, Comparator.nullsFirst(Comparator.naturalOrder()));
		
		rtnList =  rtnList.stream().sorted(compareByYearThenMonth.reversed()).collect(Collectors.toList());
		commonService.addUserDetailsToDTO(rtnList, PaymentDetailsDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public ApiResponseEntity getDuesListView(String reqParams) throws Exception {
		List<PaymentDetailsDTO> rtnList = new ArrayList<>();
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		
		if(reqParamDto.getParentRecordId() <= 0) {
			RequestParamDTO reqParamDtoTemp = RequestParamDTO.getInstance(reqParams);
			if(reqParamDtoTemp.getSeacrchFields() != null) {
				reqParamDtoTemp.setSeacrchFields(reqParamDtoTemp.getSeacrchFields().stream().filter( f -> "flatNo".equals(f.getDataField())).toList());
			}
			List<FlatDetailsDTO> list = dbQueryExecuter.executeQuery(new QueryMaker<FlatDetailsDTO>(reqParamDtoTemp, FlatDetailsDTO.class));
			
			for(FlatDetailsDTO dto : list) {
				rtnList.addAll(getDuesList(dto.getId(), new Object[] {dto.getFlatNo()}));
			}
			long id = 1;
			for(PaymentDetailsDTO dto : rtnList) {
				dto.setId(id++);
			}
		} else {
			rtnList = getDuesList(reqParamDto.getParentRecordId());
		}
		
		if(reqParamDto.getSeacrchFields() != null) {
			List<ListViewFieldDTO> filterList = reqParamDto.getSeacrchFields().stream().filter( f -> !"flatNo".equals(f.getDataField())).collect(Collectors.toList());
					
			for(ListViewFieldDTO fieldDto: filterList) {
				try {
					if(fieldDto != null && fieldDto.getValue() != null) {
						if("amount".equals(fieldDto.getDataField()) && !fieldDto.getValue().toString().trim().isEmpty()) {
							rtnList = rtnList.stream().filter( f -> f.getAmount() == Double.parseDouble(fieldDto.getValue().toString())).collect(Collectors.toList());
						} else if("paymentForSessionName".equals(fieldDto.getDataField())) {
							rtnList = rtnList.stream().filter( f -> f.getPaymentForSessionName().contains(fieldDto.getValue().toString())).collect(Collectors.toList());
						} else if("paymentMonthName".equals(fieldDto.getDataField())) {
							rtnList = rtnList.stream().filter( f -> f.getPaymentMonthName().contains(fieldDto.getValue().toString())).collect(Collectors.toList());
						} else if("paymentYear".equals(fieldDto.getDataField()) && !fieldDto.getValue().toString().trim().isEmpty()) {
							rtnList = rtnList.stream().filter( f -> f.getPaymentYear() == Integer.parseInt(fieldDto.getValue().toString())).collect(Collectors.toList());
						} 
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public void isDuplicatePaymentDetailsFound(List<PaymentDetailsDTO> list, Long flatId) {
		UserContext userContext = Utils.getUserContext();
		
		List<PaymentDetailsEntity> paidList = paymentDetailsRepository.findByFlatIdForDueList(userContext.getApartmentId(), flatId);
		paidList = paidList.stream().filter( f -> f.getEventId() == 1).collect(Collectors.toList());
		
		for(PaymentDetailsDTO dto : list) {
			long count = paidList.stream().filter( f -> f.getPaymentForSessionId().equals(dto.getPaymentForSessionId())
										&& f.getPaymentMonth().equals(dto.getPaymentMonth())
										&& f.getPaymentYear().equals(dto.getPaymentYear())).count();
			if(count > 0) {
				String sessionName = sessionDetailsRepository.getSessionName(userContext.getApartmentId(), dto.getPaymentForSessionId());
				throw new RuntimeException("Already paid for session: " + sessionName + " & Month: " +dto.getPaymentMonth()+"-"+dto.getPaymentYear());
			}
		}
	}
	
	@Override
	public List<PaymentDetailsDTO> getDuesList(Long flatId, Object ...arr) {
		UserContext userContext = Utils.getUserContext();
		
		String flatNo = null; 
		if(arr != null && arr.length > 0) {
			flatNo = String.valueOf(arr[0]);
		} else {
			FlatDetailsEntity flatDetailsEntity = flatDetailsRepository.findById(flatId).orElse(null);
			flatNo = flatDetailsEntity != null ? flatDetailsEntity.getFlatNo() : "";
		}
		List<PaymentDetailsDTO> duesList = new ArrayList<>();
		
		PaymentDetailsEntity lastPayment = getLastPaymentForMaintenance(flatId);
		
		LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
		SessionDetailsEntity sessionDtls = sessionDetailsRepository.findById(userContext.getSessionId()).get();
		if(sessionDtls != null) {
			if(lastPayment == null && sessionDtls.getFromDate() != null) {
				
				LocalDate localDate = sessionDtls.getFromDate().toInstant()
					      .atZone(ZoneId.systemDefault())
					      .toLocalDate();
				
				lastPayment = new PaymentDetailsEntity();
				lastPayment.setPaymentYear(localDate.getYear());
				lastPayment.setPaymentMonth(localDate.getMonth().getValue()-1);
				
			}
			if(sessionDtls.getToDate() != null) {
				LocalDate toDt = sessionDtls.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				//currentDate = currentDate.isAfter(toDt) ? toDt : currentDate; //currently paused
				currentDate = toDt;
			}
		}
		
		
		if(lastPayment != null && currentDate != null) {
			
			Integer year = lastPayment.getPaymentYear();
			Integer month = lastPayment.getPaymentMonth()+1;
			
			Integer currentYear = currentDate.getYear();
			Integer currentMonth = currentDate.getMonth().getValue();
			
			PaymentDetailsDTO payDtlsDto = null;
			
			long tempRecId = 1;
			int tempfromMonth = month;
			int tempToMonth = 12;
			for(int yr = year; yr <= currentYear; yr++) {
				
				if(yr == currentYear) {
					tempToMonth = currentMonth;
				}
				for(int mn = tempfromMonth; mn <= tempToMonth; mn++) {
					payDtlsDto = new PaymentDetailsDTO();
					payDtlsDto.setId(tempRecId++);
					payDtlsDto.setFlatId(flatId);
					payDtlsDto.setFlatNo(flatNo);
					payDtlsDto.setPaymentMonth(mn);
					payDtlsDto.setPaymentMonthName(DateUtils.MONTH_NAME_MAP.getOrDefault(mn, ""));
					payDtlsDto.setPaymentYear(yr);
					duesList.add(payDtlsDto);
				}
				tempfromMonth = 1;
			}
			
		} else {
			throw new RuntimeException("Error on create due list.");
		}
		
		if(duesList != null && !duesList.isEmpty()) {
			sessionDetailsService.addSessionIdAndMaintenanceOnList(duesList, flatId);
		}
		
		return duesList;
	}

	@Override
	public ApiResponseEntity saveLastPaymentDate(@NonNull PaymentDetailsDTO paymentDetailsDto) {
		UserContext userContext = Utils.getUserContext();
		
		if(paymentDetailsDto.getFlatId() > 0) {
			PaymentDetailsEntity entity = new PaymentDetailsEntity();
			
			List<PaymentDetailsEntity> list = paymentDetailsRepository.getMaintenanceList(userContext.getApartmentId(), paymentDetailsDto.getFlatId());
			if(list != null && !list.isEmpty()) {
				PaymentDetailsEntity pdEntity = list.stream().filter( f -> ApiConstants.INVALID_RECORD_ID == f.getPaymentId() 
										&& (f.getPaymentForSessionId() == null || f.getPaymentForSessionId() == 0)
										&& (f.getAmount() == null || f.getAmount() == 0)
										&& (f.getSessionId() == null || f.getSessionId() == 0) ).findFirst().orElse(null);
				if(pdEntity != null) {
					entity = pdEntity;
					BeanUtils.copyProperties(paymentDetailsDto, entity, Utils.getIgnoreEntityPropsOnUpdate(new String[] {"id", "paymentId", "paymentForSessionId"}));
					entity.setModifiedBy(userContext.getUserId());
				} else {
					throw new RuntimeException("Already payment exists. Please try on another record.");
				}
			} else {
				BeanUtils.copyProperties(paymentDetailsDto, entity);
				entity.setPaymentId(new Long(ApiConstants.INVALID_RECORD_ID));
				entity.setPaymentForSessionId(new Long(0));
				entity.setCreatedBy(userContext.getUserId());
				entity.setModifiedBy(userContext.getUserId());
				entity.setApartmentId(userContext.getApartmentId());
			}
			
			paymentDetailsRepository.save(entity);
			
			BeanUtils.copyProperties(entity, paymentDetailsDto);	
		}
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, paymentDetailsDto);
	}
	
	@Override
	public PaymentDetailsEntity getLastPaymentForMaintenance(Long flatId) {
		UserContext userContext = Utils.getUserContext();
		
		PaymentDetailsEntity lastPayment = null;
		List<PaymentDetailsEntity> list = paymentDetailsRepository.findByFlatIdForDueList(userContext.getApartmentId(), flatId);
		list = list.stream().filter( f -> f.getEventId() == 1).collect(Collectors.toList());
		if(list != null && !list.isEmpty()) {			
			Comparator<PaymentDetailsEntity> compareByYearThenMonth = Comparator
	                .comparing(PaymentDetailsEntity::getPaymentYear)
	                .thenComparing(PaymentDetailsEntity::getPaymentMonth);
		
			lastPayment =  list.stream().sorted(compareByYearThenMonth.reversed()).limit(1).findAny().orElse(null);
		}
		return lastPayment;
	}
	
	@Override
	public ApiResponseEntity getDuesListForAdvancePayment(Long flatId, int month, int year) throws Exception {
		UserContext userContext = Utils.getUserContext();
		PaymentDetailsDTO payDtlsDto = null;
		if(flatId != null && flatId > 0) {
			FlatDetailsEntity flatDetailsEntity = flatDetailsRepository.findById(flatId).orElse(null);
			String flatNo = flatDetailsEntity != null ? flatDetailsEntity.getFlatNo() : "";
			
			payDtlsDto = new PaymentDetailsDTO();
			payDtlsDto.setId(Long.valueOf(1));
			payDtlsDto.setFlatId(flatId);
			payDtlsDto.setFlatNo(flatNo);
			
			if(month <= 0 && year <= 0) {
				PaymentDetailsEntity lastPayment = getLastPaymentForMaintenance(flatId);
				if(lastPayment != null) {
					month = lastPayment.getPaymentMonth();
					year = lastPayment.getPaymentYear();
				}
			}
			
			if(month <= 0 && year <= 0) {
				month = userContext.getSessionDetailsEntity().getToDate().getMonth()+1;
				year = userContext.getSessionDetailsEntity().getToDate().getYear()+1900;
			}
			
			payDtlsDto.setPaymentMonth(month == 12 ? 1 : month+1);
			payDtlsDto.setPaymentYear(month == 12 ? year+1 : year);
			
			List<PaymentDetailsDTO> duesList = new ArrayList<>();
			duesList.add(payDtlsDto);
			sessionDetailsService.addSessionIdAndMaintenanceOnList(duesList, flatId);
		}
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, payDtlsDto);
	}
}
