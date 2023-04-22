package com.drps.ams.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EmailServiceDTO;
import com.drps.ams.dto.EmailSetupDetailsDTO;
import com.drps.ams.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

private static final  Logger logger = LogManager.getLogger(EmailController.class);
	
	@Autowired
	EmailService emailService;
	
	
	@PostMapping(value = "/setup/create_or_update")
	public ResponseEntity<ApiResponseEntity> createOrUpdate(@RequestBody EmailSetupDetailsDTO emailSetupDetailsDTO) throws Exception {
		logger.info("AMS - EmailController createOrUpdate: {}", emailSetupDetailsDTO);
		return ResponseEntity.status(HttpStatus.OK).body(emailService.setupSaveOrUpdate(emailSetupDetailsDTO));		
	}
	
	@GetMapping(value = "/setup/get")
	public ResponseEntity<ApiResponseEntity> getSelf() throws Exception {
		logger.info("AMS - EmailController getSelf: {}");
		return ResponseEntity.status(HttpStatus.OK).body(emailService.getSetupByApartmentId());		
	}
	
	@PostMapping(value = "/service/create_or_update")
	public ResponseEntity<ApiResponseEntity> createOrUpdate(@RequestBody EmailServiceDTO dto) throws Exception {
		System.out.println("AMS - EmailServiceController createOrUpdate: {}");
		return ResponseEntity.status(HttpStatus.OK).body(emailService.serviceSaveOrUpdate(dto));		
	}

	@GetMapping(value = "/service/get")
	public ResponseEntity<ApiResponseEntity> getByApartmentId() throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(emailService.getServiceByApartmentId());		
	}
	
}
