package com.drps.ams.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.service.CommonService;

@RestController
@RequestMapping("/api/common")
public class CommonController {

	private static final  Logger logger = LogManager.getLogger(CommonController.class);
	
	@Autowired
	CommonService commonService;
	
	@PostMapping(value = "/link/save")
	public ResponseEntity<ApiResponseEntity> saveLinkObject(@RequestBody RequestParamDTO reqParams) throws Exception {
		logger.info("AMS - CommonController -> saveLinkObject");
		return ResponseEntity.status(HttpStatus.OK).body(commonService.saveLinkObject(reqParams));		
	}
	
	@GetMapping(value = "/link/get")
	public ResponseEntity<ApiResponseEntity> getLinkObjectDetails(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - CommonController -> getLinkObjectDetails");
		return ResponseEntity.status(HttpStatus.OK).body(commonService.getLinkObjectDetails(reqParams));		
	}
	
}
