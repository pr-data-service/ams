package com.drps.ams.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.service.ExpenseItemsService;

@RestController
@RequestMapping("/api/expense_items")
public class ExpenseItemsController {

	private static final  Logger logger = LogManager.getLogger(ExpenseItemsController.class);
	
	@Autowired
	ExpenseItemsService expenseItemsService;
	
	@GetMapping(value = "/list_view/get")
	public ResponseEntity<ApiResponseEntity> getListView(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - ExpenseItemsController getListView");		
		return ResponseEntity.status(HttpStatus.OK).body(expenseItemsService.getListView(reqParams));		
	}
}
