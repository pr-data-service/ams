package com.drps.ams.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ProjectDTO;
import com.drps.ams.service.ProjectService;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

	private static final  Logger logger = LogManager.getLogger(ProjectController.class);
	
	@Autowired
	ProjectService projectService;
	
	@PostMapping(value = "/create_or_update")
	public ResponseEntity<ApiResponseEntity> createOrUpdate(@RequestBody ProjectDTO projectDTO) throws Exception {
		logger.info("AMS - ProjectController createOrUpdate: {}", projectDTO);
		return ResponseEntity.status(HttpStatus.OK).body(projectService.saveOrUpdate(projectDTO));		
	}
	
	@GetMapping(value = "/get")
	public ResponseEntity<ApiResponseEntity> get() throws Exception {
		logger.info("AMS - ProjectController get");
		return ResponseEntity.status(HttpStatus.OK).body(projectService.get());		
	}
	
//	@GetMapping(value = "/session/list/get")
//	public ResponseEntity<ApiResponseEntity> get(@RequestHeader("session-id") Long sessionId) throws Exception {
//		logger.info("AMS - ProjectController get");
//		final String requestTokenHeader = request.getHeader("Authorization");
//		return ResponseEntity.status(HttpStatus.OK).body(projectService.getSessionList(requestTokenHeader));		
//	}
}
