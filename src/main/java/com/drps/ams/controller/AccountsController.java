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

import com.drps.ams.dto.AccountTransactionDTO;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.OpeningBalanceDTO;
import com.drps.ams.service.AccountsService;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
	
	private static final  Logger logger = LogManager.getLogger(AccountsController.class);
			
	@Autowired
	AccountsService accountsService;
	
	@GetMapping(value = "/transaction/list/get")
	public ResponseEntity<ApiResponseEntity> getTransaction() {
		logger.info("AMS - AccountsController get");
		return ResponseEntity.status(HttpStatus.OK).body(accountsService.get());		
	}
	
	@PostMapping(value = "/transaction/create")
	public ResponseEntity<ApiResponseEntity> createTransaction(@RequestBody @NonNull AccountTransactionDTO dto) throws Exception {
		logger.info("AMS - AccountsController createTransaction");
		return ResponseEntity.status(HttpStatus.OK).body(accountsService.saveOrUpdate(dto));		
	}
	@DeleteMapping(value = "/transaction/delete/{id}")
	public ResponseEntity<ApiResponseEntity> deleteById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - AccountsController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(accountsService.deleteById(id));		
	}
	
	@PostMapping(value = "/opening-balance/create-or-update")
	public ResponseEntity<ApiResponseEntity> createOrUpdate(@RequestBody @NonNull OpeningBalanceDTO dto) throws Exception {
		logger.info("AMS - AccountsController createOrUpdate");
		return ResponseEntity.status(HttpStatus.OK).body(accountsService.openingBalanceSaveOrUpdate(dto));		
	}
	
	@GetMapping(value = "/opening-balance/get")
	public ResponseEntity<ApiResponseEntity> getOpeningBalance() throws Exception {
		logger.info("AMS - AccountsController getOpeningBalance");
		return ResponseEntity.status(HttpStatus.OK).body(accountsService.getOpeningBalance());
	}
	
	@GetMapping(value = "/payment-info/get")
	public ResponseEntity<ApiResponseEntity> getPaymentInfo() {
		logger.info("AMS - AccountsController getPaymentInfo");
		return ResponseEntity.status(HttpStatus.OK).body(accountsService.getOpeningBalance());
	}
	
	@GetMapping(value = "/expense-info/get")
	public ResponseEntity<ApiResponseEntity> getExpenseInfo() {
		logger.info("AMS - AccountsController getExpenseInfo");
		return ResponseEntity.status(HttpStatus.OK).body(accountsService.getExpenseInfo());
	}
	
}
