package com.drps.ams.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
import com.drps.ams.entity.PaymentEntity;
import com.drps.ams.repository.EventsRepository;
import com.drps.ams.repository.ExpenseItemsRepository;
import com.drps.ams.repository.ExpensesRepository;
import com.drps.ams.repository.FlatDetailsRepository;
import com.drps.ams.repository.PaymentDetailsRepository;
import com.drps.ams.repository.PaymentRepository;
import com.drps.ams.service.DashboardService;
import com.drps.ams.service.EventsService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DateUtils;
import com.drps.ams.util.Utils;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	PaymentRepository paymentRepository;	
	
	@Autowired
	PaymentDetailsRepository paymentDetailsRepository;
	
	@Autowired
	EventsRepository eventsRepository;
	
	@Autowired
	ExpensesRepository expensesRepository;
	
	@Autowired
	FlatDetailsRepository flatDetailsRepository;
	
	@Override
	public ApiResponseEntity getMonthlyMaintenance() {
		UserContext userContext = Utils.getUserContext();
		
		
		Map<Integer, Double> map = getTotalMaintenance();
		
		
		List<Map<String, Object>> rsltList = new ArrayList<>();
		Map<String, Object> mapObj;
		
		for(Integer month : map.keySet()) {
			mapObj = new HashMap<>();
			mapObj.put("month", month);
			mapObj.put("Amount", map.get(month));
			rsltList.add(mapObj);
		}
		// TODO Auto-generated method stub
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rsltList);
	}
	
	@Override
	public ApiResponseEntity getEventsReport() {
		UserContext userContext = Utils.getUserContext();
		
		
		List<EventsEntity> eventList = eventsRepository.getAllActiveEntity(userContext.getApartmentId());
		List<PaymentDetailsEntity> list = paymentDetailsRepository.getEventPaymentList(userContext.getApartmentId());
		List<ExpensesEntity> expenseList = expensesRepository.getAll(userContext.getApartmentId(), userContext.getSessionId());
		
		
		List<Map<String, Object>> rsltList = new ArrayList<>();
		Map<String, Object> mapObj;
		for(EventsEntity entity: eventList) {
			if(entity != null) {
				mapObj = new HashMap<>();
				mapObj.put("name", entity.getName());
				mapObj.put("expanse", expenseList.stream().filter( f -> f.getEventId() == entity.getId()).mapToDouble( d -> d.getAmount()).sum());
				mapObj.put("collection", list.stream().filter( f -> f.getEventId() == entity.getId()).mapToDouble( d -> d.getAmount()).sum());
				rsltList.add(mapObj);
			}
		}
		
		// TODO Auto-generated method stub
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rsltList);
	}
	
	@Override
	public ApiResponseEntity getTodaysCollection() {
		UserContext userContext = Utils.getUserContext();
		
		Map<Long, Double> map = getTodaysMaintenance();
		
		
		List<Map<String, Object>> rsltList = new ArrayList<>();
		Map<String, Object> mapObj;
		
		for(Long flatId : map.keySet()) {
			mapObj = new HashMap<>();
			mapObj.put("name", flatDetailsRepository.findById(flatId).get().getFlatNo());
			mapObj.put("Amount", map.get(flatId));
			rsltList.add(mapObj);
		}
		// TODO Auto-generated method stub
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rsltList);
	}
	
	public Map<Integer, Double> getTotalMaintenance() {
		UserContext userContext = Utils.getUserContext();
		List<PaymentDetailsEntity> list = paymentDetailsRepository.getMaintenanceDetailsList(userContext.getApartmentId(), userContext.getSessionId());
		
		Map<Integer, Double> map = new HashMap<>();
		for(PaymentDetailsEntity entity : list) {
			
			if(entity.getPaymentDate() != null && entity.getPaymentDate().getMonth() >= 0) {
				if(map.containsKey(entity.getPaymentDate().getMonth())) {
					map.put(entity.getPaymentDate().getMonth(), map.get(entity.getPaymentDate().getMonth()) + entity.getAmount());
				} else {
					map.put(entity.getPaymentDate().getMonth(), entity.getAmount());
				}
			}
		}
		
		return map;
	}
	
	public Map<Long, Double> getTodaysMaintenance() {System.out.println(DateUtils.stringToDateTime("04-09-2022 00:00:00"));
		UserContext userContext = Utils.getUserContext();
		String startDt = DateUtils.dateToStringForDB(null) + " 00:00:00";
		String endDt = DateUtils.dateToStringForDB(null) + " 23:59:59";
		List<PaymentEntity> list = paymentRepository.getTodaysPaymentList(userContext.getApartmentId(), userContext.getSessionId(), DateUtils.stringToDateTimeForDB(startDt), DateUtils.stringToDateTimeForDB(endDt));
		list = list.stream().filter( f -> f.getAmount() != null).collect(Collectors.toList());
		
		Map<Long, Double> map = new HashMap<>();
		for(PaymentEntity entity : list) {
			
			if(entity.getPaymentDate() != null && entity.getFlatId() > 0 && entity.getAmount() > 0) {
				if(map.containsKey(entity.getFlatId())) {
					map.put(entity.getFlatId(), map.get(entity.getFlatId()) + entity.getAmount());
				} else {
					map.put(entity.getFlatId(), entity.getAmount());
				}
			}
		}
		
		return map;
	}
	
	

}
