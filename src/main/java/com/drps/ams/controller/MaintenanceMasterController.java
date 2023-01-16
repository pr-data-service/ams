package com.drps.ams.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.dto.MaintenanceMasterDTO;
import com.drps.ams.service.MaintenanceMasterService;
import com.drps.ams.util.Utils;

@RestController
@RequestMapping("/api/maintenance/master")
public class MaintenanceMasterController {

	private static final  Logger logger = LogManager.getLogger(MaintenanceMasterController.class);
	
	@Autowired
	MaintenanceMasterService maintenanceMasterService;
	
	@PostMapping(value = "/create_or_update")
	public ResponseEntity<ApiResponseEntity> createOrUpdate(@RequestBody MaintenanceMasterDTO maintenanceMasterDTO) throws Exception {
		logger.info("AMS - MaintenanceMasterController createOrUpdate: {}", maintenanceMasterDTO);
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceMasterService.saveOrUpdate(maintenanceMasterDTO));		
	}
	
	@GetMapping(value = "/list_view/get")
	public ResponseEntity<ApiResponseEntity> getListView(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - MaintenanceMasterController getListView");		
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceMasterService.getListView(reqParams));		
	}
	
	@GetMapping(value = "/list/get")
	public ResponseEntity<ApiResponseEntity> get() throws Exception {
		logger.info("AMS - MaintenanceMasterController get");
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceMasterService.get());		
	}
	
	@GetMapping(value = "/get/{id}")
	public ResponseEntity<ApiResponseEntity> getById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - MaintenanceMasterController getById");
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceMasterService.getById(id));		
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<ApiResponseEntity> deleteById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - MaintenanceMasterController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceMasterService.deleteById(id));		
	}
	
	@DeleteMapping(value = "/delete/batch")
	public ResponseEntity<ApiResponseEntity> deleteAllById(@RequestBody List<Long> ids) throws Exception {
		logger.info("AMS - MaintenanceMasterController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceMasterService.deleteAllById(ids));		
	}
	
	@GetMapping(value = "/active/{id}")
	public ResponseEntity<ApiResponseEntity> activeInActive(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - MaintenanceMasterController activeInActive");
		return ResponseEntity.status(HttpStatus.OK).body(maintenanceMasterService.activeInActive(id));		
	}
	
}
