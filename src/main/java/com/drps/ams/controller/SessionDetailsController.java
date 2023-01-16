package com.drps.ams.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.SessionDetailsDTO;
import com.drps.ams.service.SessionDetailsService;

@RestController
@RequestMapping("/api/session_details")
public class SessionDetailsController {
	
	private static final  Logger logger = LogManager.getLogger(SessionDetailsController.class);
	
	@Autowired
	SessionDetailsService sessionDetailsService;
	
	@GetMapping(value = "/list/get")
	public ResponseEntity<ApiResponseEntity> get() throws Exception {
		logger.info("AMS - SessionDetailsController get");
		return ResponseEntity.status(HttpStatus.OK).body(sessionDetailsService.get());		
	}
	
	@PostMapping(value = "create_or_update")
	public ResponseEntity<ApiResponseEntity> createORUpdate(@RequestBody @NonNull SessionDetailsDTO dto) throws Exception {
		logger.info("AMS - SessionDetailsController createORUpdate");
		return ResponseEntity.status(HttpStatus.OK).body(sessionDetailsService.saveOrUpdate(dto));		
	}
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<ApiResponseEntity> deleteById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - SessionDetailsController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(sessionDetailsService.deleteById(id));		
	}
}
