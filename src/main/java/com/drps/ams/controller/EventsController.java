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
import com.drps.ams.dto.EventsDTO;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.service.EventsService;

@RestController
@RequestMapping("/api/events")
public class EventsController {
	private static final  Logger logger = LogManager.getLogger(EventsController.class);
	
	@Autowired
	EventsService eventsService;
	
	@PostMapping(value = "/create_or_update")
	public ResponseEntity<ApiResponseEntity> createOrUpdate(@RequestBody EventsDTO eventsDTO) throws Exception {
		logger.info("AMS - EventsController createOrUpdate: {}", eventsDTO);
		return ResponseEntity.status(HttpStatus.OK).body(eventsService.saveOrUpdate(eventsDTO));		
	}
	
	@GetMapping(value = "/list_view/get")
	public ResponseEntity<ApiResponseEntity> getListView(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - EventsController getListView");		
		return ResponseEntity.status(HttpStatus.OK).body(eventsService.getListView(reqParams));		
	}
	
	@GetMapping(value = "/list/get")
	public ResponseEntity<ApiResponseEntity> get(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - EventsController get");
		return ResponseEntity.status(HttpStatus.OK).body(eventsService.get(reqParams));		
	}
	
	@GetMapping(value = "/get/{id}")
	public ResponseEntity<ApiResponseEntity> getById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - EventsController getById");
		return ResponseEntity.status(HttpStatus.OK).body(eventsService.getById(id));		
	}
	
	@GetMapping(value = "/active/{id}")
	public ResponseEntity<ApiResponseEntity> active(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - EventsController active");
		return ResponseEntity.status(HttpStatus.OK).body(eventsService.activeInActive(id, true));		
	}
	
	@GetMapping(value = "/inactive/{id}")
	public ResponseEntity<ApiResponseEntity> inActive(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - EventsController inActive");
		return ResponseEntity.status(HttpStatus.OK).body(eventsService.activeInActive(id, false));		
	}
	
	@DeleteMapping(value = "/delete/batch")
	public ResponseEntity<ApiResponseEntity> deleteAllById(@RequestBody List<Long> ids) throws Exception {
		logger.info("AMS - EventsController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(eventsService.deleteAllById(ids));		
	}
	
	@GetMapping(value = "/by_expense_id/get/{id}")
	public ResponseEntity<ApiResponseEntity> getByExpenseId(@PathVariable("id") Long expenseId) throws Exception {
		logger.info("AMS - EventsController getByExpenseId");
		return ResponseEntity.status(HttpStatus.OK).body(eventsService.getByExpenseId(expenseId));
	}
}
