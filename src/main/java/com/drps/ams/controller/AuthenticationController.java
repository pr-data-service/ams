package com.drps.ams.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.drps.ams.service.ProjectService;
import com.drps.ams.service.UserDetailsService;
import com.drps.ams.service.impl.*;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.Utils;
import com.drps.ams.security.JwtTokenUtil;
import com.drps.ams.bean.UserContext;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.JwtRequestDTO;
import com.drps.ams.dto.JwtResponseDTO;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.exception.InvalidCredentialsException;
import com.drps.ams.exception.UserDisabledException;

@RequestMapping("/api")
@RestController
@CrossOrigin
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsServiceImpl userDetailsService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	UserDetailsService amsUserDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequestDTO authenticationRequest, @RequestHeader(value = "is-session-list-required", required=false) boolean isSessionListRequired) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserContext userContext = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userContext);
		
		Map<String, Object> map = new HashMap<>();
		map.put("token", token);
		map.put("role", userContext.getUserDetailsEntity().getRole());
		if(ApiConstants.USER_ROLE_SADMIN.equals(userContext.getUserDetailsEntity().getRole())) {
			map.put("apartmentList", projectService.getApartmentList());
		}
		
		if(isSessionListRequired) {
			map.put("sessionList", projectService.getSessionList(userContext.getApartmentId()));
			map.put("permission", amsUserDetailsService.getUserPermissions(userContext.getApartmentId(), userContext.getUserDetailsEntity().getRole()));
		}

		ApiResponseEntity apiRespEntity = new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, map);
		
		return ResponseEntity.status(HttpStatus.OK).body(apiRespEntity);	
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new UserDisabledException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException("INVALID_CREDENTIALS", e);
		}
	}
}
