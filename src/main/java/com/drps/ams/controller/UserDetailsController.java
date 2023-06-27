package com.drps.ams.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.UserDetailsDTO;
import com.drps.ams.dto.UserPasswordDTO;
import com.drps.ams.dto.UserRolePermissionDTO;
import com.drps.ams.service.UserDetailsService;
import com.drps.ams.service.UserRolePermissionService;


@RestController
@RequestMapping("/api/user")
public class UserDetailsController {	
	
	private static final  Logger logger = LogManager.getLogger(UserDetailsController.class);
	
	@Autowired
	UserDetailsService userDetailsService;	
	
	@Autowired
	UserRolePermissionService userRolePermissionService;
	
	@PostMapping(value = "/create_or_update")
	public ResponseEntity<ApiResponseEntity> createOrUpdate(@RequestBody UserDetailsDTO userDetailsDTO) throws Exception {
		logger.info("AMS - UserDetailsController createOrUpdate: {}", userDetailsDTO);
		return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.saveOrUpdate(userDetailsDTO));		
	}
	
	@GetMapping(value = "/list_view/get")
	public ResponseEntity<ApiResponseEntity> getListView(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - UserDetailsController getListView");
		return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.getListView(reqParams));		
	}
	
	@GetMapping(value = "/list/get")
	public ResponseEntity<ApiResponseEntity> get() throws Exception {
		logger.info("AMS - UserDetailsController get");
		return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.get());		
	}
	
	@GetMapping(value = "/get/{id}")
	public ResponseEntity<ApiResponseEntity> getById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - UserDetailsController getById");
		return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.getById(id));		
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<ApiResponseEntity> deleteById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - UserDetailsController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.deleteById(id));		
	}
	
	@DeleteMapping(value = "/delete/batch")
	public ResponseEntity<ApiResponseEntity> deleteAllById(@RequestBody List<Long> ids) throws Exception {
		logger.info("AMS - UserDetailsController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.deleteAllById(ids));		
	}
	
	@GetMapping(value = "/by_flat_id/get/{id}")
	public ResponseEntity<ApiResponseEntity> getByFlatId(@PathVariable("id") Long flatId) throws Exception {
		logger.info("AMS - UserDetailsController getByFlatId");
		return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.getByFlatId(flatId));
	}
	
	@GetMapping(value = "/loggedin/get")
	public ResponseEntity<ApiResponseEntity> getLoggedInUder() throws Exception {
		logger.info("AMS - UserDetailsController getLoggedInUder");
		return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.getLoggedInUder());		
	}

	@PostMapping(value = "/update_password")
	public ResponseEntity<ApiResponseEntity> updatePassword(@RequestBody UserPasswordDTO userPasswordDTO) throws Exception {
		logger.info("AMS - UserDetailsController updatePassword: {}", userPasswordDTO);
		
		return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.updatePassword(userPasswordDTO));		
	}
	
	@PostMapping(value = "/signature/upload")
	public ResponseEntity<ApiResponseEntity> uploadSignature(@RequestParam("file") MultipartFile file) throws Exception {
		logger.info("AMS - UserDetailsController uploadSignature");
		return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.uploadSignature(file));		
	}
	
	@GetMapping(value = "/signature/get", produces = MediaType.IMAGE_JPEG_VALUE)
	public void getSignature(HttpServletRequest req, HttpServletResponse res) throws Exception {
		logger.info("AMS - UserDetailsController getSignature");
		
		userDetailsService.getSignature(req, res);
	}
	
	@PostMapping(value = "/user-role-permission/create_or_update")
	public ResponseEntity<ApiResponseEntity> createOrUpdateUserRolePermission(@RequestBody UserRolePermissionDTO dto) throws Exception {
		logger.info("AMS - UserDetailsController createOrUpdateUserRolePermission: {}", dto);
		return ResponseEntity.status(HttpStatus.OK).body(userRolePermissionService.saveOrUpdateUserRolePermission(dto));		
	}
	
	@GetMapping(value = "/user-role-permission/get")
	public ResponseEntity<ApiResponseEntity> getUserRolePermission() {
		logger.info("AMS - UserDetailsController getUserRolePermission");
		return ResponseEntity.status(HttpStatus.OK).body(userRolePermissionService.getUserRolePermission());		
	}
	
	@GetMapping(value = "/user-role-permission/get/{object}/{role}")
	public ResponseEntity<ApiResponseEntity> getUserRolePermission(@PathVariable String object, @PathVariable String role) {
		logger.info("AMS - UserDetailsController getUserRolePermission");
		return ResponseEntity.status(HttpStatus.OK).body(userRolePermissionService.getUserRolePermission(object, role));		
	}
}
