package com.drps.ams.controller;

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
import com.drps.ams.service.EmailSignatureService;

import java.util.List;


@RestController
@RequestMapping(value= "/api/email/service")
public class EmailServiceController {
	
	@Autowired
	EmailSignatureService emailSignatureService;
	
	@PostMapping(value = "/create_or_update")
	public ResponseEntity<ApiResponseEntity> createOrUpdate(@RequestBody EmailServiceDTO dto) throws Exception {
		System.out.println("AMS - EmailServiceController createOrUpdate: {}");
		return ResponseEntity.status(HttpStatus.OK).body(emailSignatureService.saveOrUpdate(dto));		
	}
	
	@GetMapping(value = "/get")
	public ResponseEntity<ApiResponseEntity> getByApartmentId() throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(emailSignatureService.getByApartmentId());		
	}
}
