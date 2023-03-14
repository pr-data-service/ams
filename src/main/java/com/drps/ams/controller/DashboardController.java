package com.drps.ams.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.service.DashboardService;


@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	private static final  Logger logger = LogManager.getLogger(DashboardController.class);
	
	@Autowired
	DashboardService dashboardService;
	
	@GetMapping(value = "/maintenance/monthly/get")
	public ResponseEntity<ApiResponseEntity> getMonthlyMaintenance() throws Exception {
		logger.info("AMS - DashboardController getMonthlyMaintenance");		
		return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getMonthlyMaintenance());		
	}
	
	@GetMapping(value = "/events/report/get")
	public ResponseEntity<ApiResponseEntity> getEventsReport() throws Exception {
		logger.info("AMS - DashboardController getEventsReport");		
		return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getEventsReport());		
	}
	
	@GetMapping(value = "/maintenance/todays/get")
	public ResponseEntity<ApiResponseEntity> getTodaysCollection() throws Exception {
		logger.info("AMS - DashboardController getTodaysMaintenance");		
		return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getTodaysCollection());		
	}
}
