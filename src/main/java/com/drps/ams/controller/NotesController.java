package com.drps.ams.controller;

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
import com.drps.ams.dto.NotesDTO;
import com.drps.ams.service.NotesService;

@RestController
@RequestMapping("/api/notes")
public class NotesController {

	private static final  Logger logger = LogManager.getLogger(NotesController.class);
	
	
	@Autowired
	NotesService notesService;	
	
	@PostMapping(value = "/create_or_update")
	public ResponseEntity<ApiResponseEntity> createOrUpdate(@RequestBody NotesDTO dto) throws Exception {
		logger.info("AMS - NotesController createOrUpdate: {}", dto);
		return ResponseEntity.status(HttpStatus.OK).body(notesService.saveOrUpdate(dto));		
	}
	
	@GetMapping(value = "/view/get")
	public ResponseEntity<ApiResponseEntity> getListView(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - NotesController getListView");		
		return ResponseEntity.status(HttpStatus.OK).body(notesService.getListView(reqParams));		
	}
	
	@GetMapping(value = "/get/{id}")
	public ResponseEntity<ApiResponseEntity> getById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - NotesController get");
		return ResponseEntity.status(HttpStatus.OK).body(notesService.get(id));		
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<ApiResponseEntity> deleteById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - FlatDetailsController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(notesService.deleteById(id));		
	}
	
}
