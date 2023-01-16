/**
 * 
 */
package com.drps.ams.controller;

import java.util.List;

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
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.dto.UserDetailsDTO;
import com.drps.ams.service.FlatDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author 002ZX2744
 *
 */
@RestController
@RequestMapping("/api/flat_details")
public class FlatDetailsController {
	
	private static final  Logger logger = LogManager.getLogger(FlatDetailsController.class);
	
	
	@Autowired
	FlatDetailsService flatDetailsService;	
	
	@PostMapping(value = "/create_or_update")
	public ResponseEntity<ApiResponseEntity> createOrUpdate(@RequestBody FlatDetailsDTO flatDetailsDTO) throws Exception {
		logger.info("AMS - FlatDetailsController createOrUpdate: {}", flatDetailsDTO);
		return ResponseEntity.status(HttpStatus.OK).body(flatDetailsService.saveOrUpdate(flatDetailsDTO));		
	}
	
	@GetMapping(value = "/list_view/get")
	public ResponseEntity<ApiResponseEntity> getListView(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - FlatDetailsController getListView");		
		return ResponseEntity.status(HttpStatus.OK).body(flatDetailsService.getListView(reqParams));		
	}
	
	@GetMapping(value = "/list/get")
	public ResponseEntity<ApiResponseEntity> get() throws Exception {
		logger.info("AMS - FlatDetailsController get");
		return ResponseEntity.status(HttpStatus.OK).body(flatDetailsService.get());		
	}
	
	@GetMapping(value = "/get/{id}")
	public ResponseEntity<ApiResponseEntity> getById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - FlatDetailsController getById");
		return ResponseEntity.status(HttpStatus.OK).body(flatDetailsService.getById(id));		
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<ApiResponseEntity> deleteById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - FlatDetailsController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(flatDetailsService.deleteById(id));		
	}
	
	@DeleteMapping(value = "/delete/batch")
	public ResponseEntity<ApiResponseEntity> deleteAllById(@RequestBody List<Long> ids) throws Exception {
		logger.info("AMS - FlatDetailsController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(flatDetailsService.deleteAllById(ids));		
	}
}
