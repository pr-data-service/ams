package com.drps.ams.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
import com.drps.ams.cache.FlatDetailsCacheService;
import com.drps.ams.dbquery.DBQueryExecuter;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ExpensesDTO;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.dto.ListViewFieldDTO;
import com.drps.ams.dto.MaintenanceDTO;
import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.entity.ApartmentDetailsEntity;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
import com.drps.ams.entity.PaymentTypeMasterEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.exception.UserContextNotFoundException;
import com.drps.ams.repository.ApartmentDetailsRepository;
import com.drps.ams.repository.EventsRepository;
import com.drps.ams.repository.FlatDetailsRepository;
import com.drps.ams.repository.LinkFlatDetailsAndUserDetailsRepository;
import com.drps.ams.repository.MaintenanceMasterRepository;
import com.drps.ams.repository.PaymentDetailsRepository;
import com.drps.ams.repository.PaymentRepository;
import com.drps.ams.repository.PaymentTypeMasterRepository;
import com.drps.ams.repository.SessionDetailsRepository;
import com.drps.ams.service.CommonService;
import com.drps.ams.service.EmailService;
import com.drps.ams.service.FlatDetailsService;
import com.drps.ams.service.PaymentDetailsService;
import com.drps.ams.service.SessionDetailsService;
import com.drps.ams.service.helper.FlatDetailsHelperService;
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
	
	@Autowired
	FlatDetailsCacheService flatDetailsCacheService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	ApartmentDetailsRepository apartmentDetailsRepository;
	
	@Autowired
	FlatDetailsHelperService flatDetailsHelperService;
	
	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		
		emailService.getEmailDetailsForDues();
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
		UserContext userContext = Utils.getUserContext();
		List<PaymentDetailsDTO> rtnList = new ArrayList<>();
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		
		if(ApiConstants.OBJECT_EVENTS.equals(reqParamDto.getObject())) {
			Long eventId = reqParamDto.getId();
			EventsEntity eventsEntity = eventsRepository.findById(eventId).get();
			
			List<Object[]> list = paymentDetailsRepository.getEventPaymentList(userContext.getApartmentId(), eventId);
			List<Long> paymentFlatIdList = list.stream().map( m -> Long.valueOf(String.valueOf(m[0]))).collect(Collectors.toList());
			List<FlatDetailsEntity> flatNotPaymentList = flatDetailsRepository.getFlatListNotIn(userContext.getApartmentId(), paymentFlatIdList);
			
			
			PaymentDetailsDTO payDtlsDto = null;
			long tempRecId = 1;
			for(Object[] arrObj : list) {
				payDtlsDto = new PaymentDetailsDTO();
				payDtlsDto.setId(tempRecId++);
				payDtlsDto.setFlatId(Long.valueOf(String.valueOf(arrObj[0])));
				payDtlsDto.setFlatNo(String.valueOf(arrObj[1]));
				payDtlsDto.setAmount(eventsEntity.getAmountPerFlat() - Double.valueOf(String.valueOf(arrObj[2])));
				if(payDtlsDto.getAmount() > 0) {
					rtnList.add(payDtlsDto);
				}
			}
			
			for(FlatDetailsEntity entity : flatNotPaymentList) {
				payDtlsDto = new PaymentDetailsDTO();
				payDtlsDto.setId(tempRecId++);
				payDtlsDto.setFlatId(entity.getId());
				payDtlsDto.setFlatNo(entity.getFlatNo());
				payDtlsDto.setAmount(eventsEntity.getAmountPerFlat());
				if(payDtlsDto.getAmount() > 0) {
					rtnList.add(payDtlsDto);
				}
			}
			
			commonService.addOwnersNameAndContactNoToDTO(rtnList);
			
		} else {
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
		}
		
		rtnList = rtnList.stream()
		        .sorted(Comparator.comparingLong(PaymentDetailsDTO::getId))
		        .collect(Collectors.toList());
		
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
	
	/*
	 * flatNo = arr[0]
	 */
	@Override
	public List<PaymentDetailsDTO> getDuesList(Long flatId, Object ...arr) {
		UserContext userContext = Utils.getUserContext();
		
		SessionDetailsEntity sessionDtls = sessionDetailsRepository.findById(userContext.getSessionId()).get();
		PaymentDetailsEntity lastPayment = getLastPaymentForMaintenance(userContext.getApartmentId(), flatId);
		lastPayment = Utils.validateLastPayment(lastPayment, sessionDtls);
		LocalDate currentDate = Utils.getCurrentDateFromSessionDetails(sessionDtls);
		
		String flatNo = flatDetailsHelperService.getFlatNo(userContext.getApartmentId(), flatId, arr); 
		List<PaymentDetailsDTO> duesList = Utils.getDuesList(flatId, flatNo, lastPayment, currentDate);
		
		
		if(duesList != null && !duesList.isEmpty()) {
			sessionDetailsService.addSessionIdAndMaintenanceOnList(duesList, flatId);
		}
		
		return duesList;
	}
	
	@Override
	public List<PaymentDetailsDTO> getDuesListForNotification(Long apartmentId, Long flatId, String flatNo) {
		List<SessionDetailsEntity> sessionList = sessionDetailsRepository.findByApartmentId(apartmentId);
		SessionDetailsEntity sessionDtls = Utils.getCurrentSessionDetails(sessionList);
		PaymentDetailsEntity lastPayment = getLastPaymentForMaintenance(apartmentId, flatId);
		lastPayment = Utils.validateLastPayment(lastPayment, sessionDtls);
		LocalDate currentDate = Utils.getCurrentDateFromSessionDetails(sessionDtls);
		
		List<PaymentDetailsDTO> duesList = Utils.getDuesList(flatId, flatNo, lastPayment, currentDate);
		
		
		if(duesList != null && !duesList.isEmpty()) {
			sessionDetailsService.addSessionIdAndMaintenanceOnList(apartmentId, duesList, flatId);
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
	public PaymentDetailsEntity getLastPaymentForMaintenance(Long apartmentId, Long flatId) {
		PaymentDetailsEntity lastPayment = null;
		List<PaymentDetailsEntity> list = paymentDetailsRepository.findByFlatIdForDueList(apartmentId, flatId);
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
				PaymentDetailsEntity lastPayment = getLastPaymentForMaintenance(userContext.getApartmentId(), flatId);
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
	
	@Override
	public Map<Long, List<PaymentDetailsDTO>> getDueListForEmailNotification(Long apartmentId, int excludesMonthCount) {
		
		Map<Long, List<PaymentDetailsDTO>> rtnflatMap = null;
		List<FlatDetailsEntity> flatList = flatDetailsRepository.getAll(apartmentId);
		
		List<PaymentDetailsDTO> duesList = null;
		rtnflatMap = new HashMap<>();
		for(FlatDetailsEntity entity : flatList) {
			try {
				duesList = getDuesListForNotification(apartmentId, entity.getId(), entity.getFlatNo());
				duesList = duesList.stream().filter(Utils::isDueTillCurrentDateOrLessThan).collect(Collectors.toList());
				
				if(duesList.size() > excludesMonthCount) {
					long srlno = 1;
					for(PaymentDetailsDTO duesDto : duesList) {
						duesDto.setId(srlno++);
					}
					rtnflatMap.put(entity.getId(), duesList);
				}
			} catch(Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
		return rtnflatMap;
	}
}
