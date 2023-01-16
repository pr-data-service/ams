package com.drps.ams.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.drps.ams.dto.ExpensesDTO;
import com.drps.ams.service.ExpensesService;
import com.drps.ams.util.Utils;


@RestController
@RequestMapping("/api/expenses")
public class ExpensesController {

	private static final  Logger logger = LogManager.getLogger(ExpensesController.class);
	
	@Autowired
	ExpensesService expensesService;
	
	@PostMapping(value = "/create_or_update")
	public ResponseEntity<ApiResponseEntity> createOrUpdate(@RequestBody ExpensesDTO expensesDTO) throws Exception {
		logger.info("AMS - ExpensesController createOrUpdate: {}", expensesDTO);
		return ResponseEntity.status(HttpStatus.OK).body(expensesService.saveOrUpdate(expensesDTO));		
	}
	
	@GetMapping(value = "/list_view/get")
	public ResponseEntity<ApiResponseEntity> getListView(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - ExpensesController getListView");		
		return ResponseEntity.status(HttpStatus.OK).body(expensesService.getListView(reqParams));		
	}
	
	@GetMapping(value = "/list/get")
	public ResponseEntity<ApiResponseEntity> get() throws Exception {
		logger.info("AMS - ExpensesController get");
		return ResponseEntity.status(HttpStatus.OK).body(expensesService.get());		
	}
	
	@GetMapping(value = "/get/{id}")
	public ResponseEntity<ApiResponseEntity> getById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - ExpensesController getById");
		return ResponseEntity.status(HttpStatus.OK).body(expensesService.getById(id));		
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<ApiResponseEntity> deleteById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - ExpensesController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(expensesService.deleteById(id));		
	}
	
	@GetMapping(value = "/download/{id}")
	public void download(HttpServletRequest request, HttpServletResponse response, 
				@PathVariable("id") Long id,
				@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - PaymentController download");
		
		File file = expensesService.getFileToDownload(id);
		Utils.downloadPdfFile(request, response, file);
	}
}
