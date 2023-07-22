package com.drps.ams.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.drps.ams.bean.UserContext;
import com.drps.ams.entity.ApartmentDetailsEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.exception.ApartmentNotFoundException;
import com.drps.ams.exception.InvalidEmailException;
import com.drps.ams.exception.SessionNotFoundException;
import com.drps.ams.repository.ApartmentDetailsRepository;
import com.drps.ams.repository.SessionDetailsRepository;
import com.drps.ams.security.JwtTokenUtil;
import com.drps.ams.service.impl.JwtUserDetailsServiceImpl;
import com.drps.ams.util.ParameterVerifier;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class FilterHelper {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private JwtUserDetailsServiceImpl jwtUserDetailsService;
	
	@Autowired
	SessionDetailsRepository sessionDetailsRepository;
	
	@Autowired
	ApartmentDetailsRepository apartmentDetailsRepository;
	
	public String[] getToken(Log logger, HttpServletRequest request) {
		final String requestTokenHeader = request.getHeader("Authorization");
		
		
		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
				throw new InvalidEmailException("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		
		return new String[] {username, jwtToken};
	}
	
	public void validateToken(Log logger, HttpServletRequest request, String jwtToken, String username) {
		
		// Once we get the token validate it.
		if (username != null && request.getHeader("session-id") != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UserContext userContext = getUserContext(request, username);
			
			
			// if token is valid configure Spring Security to manually set
			// authentication
			if (jwtTokenUtil.validateToken(jwtToken, userContext)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
	}
	
	private UserContext getUserContext(HttpServletRequest request, String username) {
		//final String requestTokenHeader = request.getHeader("Authorization");
		final String apartmentId = request.getHeader("apartment-id");
		final String sessionId = request.getHeader("session-id");
				
		UserContext userContext = this.jwtUserDetailsService.loadUserByUsername(username);
		

		if(!StringUtils.isEmpty(apartmentId) && ParameterVerifier.getLong(apartmentId) > 0) {
			ApartmentDetailsEntity apartmentDetailsEntity = apartmentDetailsRepository.findById(Long.parseLong(apartmentId)).get();
			if(apartmentDetailsEntity == null) {
				throw new ApartmentNotFoundException();
			}
			userContext.setApartmentDetailsEntity(apartmentDetailsEntity);
		}
		
		if(!StringUtils.isEmpty(sessionId) && ParameterVerifier.getLong(sessionId) > 0) {
			SessionDetailsEntity session = sessionDetailsRepository.findById(Long.parseLong(sessionId)).get();
			if(session == null) {
				throw new SessionNotFoundException();
			}
			userContext.setSessionDetailsEntity(session);
		}
		
		return userContext;
	}

}
