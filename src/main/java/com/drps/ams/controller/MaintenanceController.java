package com.drps.ams.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.MaintenanceDTO;
import com.drps.ams.service.MaintenanceService;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {
	
	private static final  Logger logger = LogManager.getLogger(MaintenanceController.class);
	
	@Autowired
	MaintenanceService maintenanceService;	
	
	@PostMapping(value = "/create_or_update")
	public ResponseEntity<ApiResponseEntity> createOrUpdate(@RequestBody MaintenanceDTO maintenanceDTO) throws Exception {
		logger.info("AMS - MaintenanceController createOrUpdate: {}", maintenanceDTO);
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceService.saveOrUpdate(maintenanceDTO));		
	}
	
	@GetMapping(value = "/list_view/get")
	public ResponseEntity<ApiResponseEntity> getListView(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - MaintenanceController getListView");		
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceService.getListView(reqParams));		
	}
	
	@GetMapping(value = "/list/get")
	public ResponseEntity<ApiResponseEntity> get() throws Exception {
		logger.info("AMS - MaintenanceController get");
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceService.get());		
	}
	
	@GetMapping(value = "/get/{id}")
	public ResponseEntity<ApiResponseEntity> getById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - MaintenanceController getById");
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceService.getById(id));		
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<ApiResponseEntity> deleteById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - MaintenanceController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceService.deleteById(id));		
	}
	
	@DeleteMapping(value = "/delete/batch")
	public ResponseEntity<ApiResponseEntity> deleteAllById(@RequestBody List<Long> ids) throws Exception {
		logger.info("AMS - MaintenanceController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceService.deleteAllById(ids));		
	}
}
