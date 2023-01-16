package com.drps.ams.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.repository.ApartmentDetailsRepository;
import com.drps.ams.repository.SessionDetailsRepository;
import com.drps.ams.repository.UserDetailsRepository;


@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	ApartmentDetailsRepository apartmentDetailsRepository;
	
	@Autowired
	SessionDetailsRepository sessionDetailsRepository;

	@Override
	public UserContext loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<UserDetailsEntity> list = userDetailsRepository.findByContactNo1(username);
		
		if (list != null && !list.isEmpty() && username.equals(list.get(0).getContactNo1())) {
			UserDetailsEntity user = list.get(0);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			
			UserContext userContext = new UserContext(
					apartmentDetailsRepository.findById(user.getApartmentId()).get(),
					user, 
					new ArrayList<>());
			return userContext;
			
//			return new User("javainuse", 
//					"$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
//					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}
