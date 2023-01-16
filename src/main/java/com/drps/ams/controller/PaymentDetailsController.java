package com.drps.ams.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.dto.PaymentSaveDTO;
import com.drps.ams.service.PaymentDetailsService;
import com.drps.ams.service.PaymentService;


@RestController
@RequestMapping("/api/payment_details")
public class PaymentDetailsController {

	private static final  Logger logger = LogManager.getLogger(PaymentDetailsController.class);
	
	@Autowired
	PaymentDetailsService paymentDetailsService;
	
	@GetMapping(value = "/list_view/get")
	public ResponseEntity<ApiResponseEntity> getListView(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - PaymentDetailsController getListView");		
		return ResponseEntity.status(HttpStatus.OK).body(paymentDetailsService.getListView(reqParams));		
	}
	
	@GetMapping(value = "/list_view/dues/get")
	public ResponseEntity<ApiResponseEntity> getDuesListView(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - PaymentDetailsController getDuesListView");		
		return ResponseEntity.status(HttpStatus.OK).body(paymentDetailsService.getDuesListView(reqParams));		
	}
	
	@PostMapping(value = "/save")
	public ResponseEntity<ApiResponseEntity> save(@RequestBody PaymentDetailsDTO paymentDetailsDto) throws Exception {
		logger.info("AMS - PaymentDetailsController save");		
		return ResponseEntity.status(HttpStatus.OK).body(paymentDetailsService.save(paymentDetailsDto));		
	}
	
}
