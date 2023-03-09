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
	
	@PostMapping(value = "/last_payment_date/save")
	public ResponseEntity<ApiResponseEntity> saveLastPaymentDate(@RequestBody PaymentDetailsDTO paymentDetailsDto) throws Exception {
		logger.info("AMS - PaymentDetailsController save");		
		return ResponseEntity.status(HttpStatus.OK).body(paymentDetailsService.saveLastPaymentDate(paymentDetailsDto));		
	}
	
	@GetMapping(value = "/advance_payment_dues/get/{flat_id}/{month}/{year}")
	public ResponseEntity<ApiResponseEntity> getDuesListForAdvancePayment(
			@PathVariable("flat_id") Long flatId,
			@PathVariable("month") Integer month,
			@PathVariable("year") Integer year) throws Exception {
		logger.info("AMS - PaymentController getDuesListForAdvancePayment");
		return ResponseEntity.status(HttpStatus.OK).body(paymentDetailsService.getDuesListForAdvancePayment(flatId, month, year));		
	}
}
