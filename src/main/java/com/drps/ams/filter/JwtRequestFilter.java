package com.drps.ams.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.drps.ams.bean.UserContext;
import com.drps.ams.entity.ApartmentDetailsEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.exception.InvalidEmailException;
import com.drps.ams.repository.ApartmentDetailsRepository;
import com.drps.ams.repository.SessionDetailsRepository;
import com.drps.ams.security.JwtTokenUtil;
import com.drps.ams.service.impl.JwtUserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;

//@Order(Ordered.LOWEST_PRECEDENCE)
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	FilterHelper filterHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String array[] = filterHelper.getToken(logger, request);
		String username = array[0];
		String jwtToken = array[1];
		
		filterHelper.validateToken(logger, request, jwtToken, username);
		
		chain.doFilter(request, response);
	}

}