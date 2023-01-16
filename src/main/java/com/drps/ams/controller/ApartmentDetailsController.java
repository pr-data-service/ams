package com.drps.ams.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.service.ApartmentDetailsService;

@RestController
@RequestMapping("/api/apartment-details")
public class ApartmentDetailsController {

	private static final Logger logger = LogManager.getLogger(ApartmentDetailsController.class);
	
	@Autowired
	ApartmentDetailsService apartmentDetailsService;
	
	@GetMapping(value = "/get")
	public ResponseEntity<ApiResponseEntity> get() throws Exception {
		logger.info("AMS - ApartmentDetailsController get");
		return ResponseEntity.status(HttpStatus.OK).body(apartmentDetailsService.get());		
	}
	
	@GetMapping(value = "/get/{id}")
	public ResponseEntity<ApiResponseEntity> getById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - ApartmentDetailsController getById");
		return ResponseEntity.status(HttpStatus.OK).body(apartmentDetailsService.getById(id));		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
