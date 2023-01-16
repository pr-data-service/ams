package com.drps.ams.bean;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.drps.ams.dto.EventsDTO;
import com.drps.ams.entity.ApartmentDetailsEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class UserContext extends User {

	ApartmentDetailsEntity apartmentDetailsEntity;
	SessionDetailsEntity sessionDetailsEntity;
	UserDetailsEntity userDetailsEntity;
	
	private Long apartmentId;
	private Long sessionId;
	private Long userId;
	
	private String userFullName;
	
	public UserContext(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		// TODO Auto-generated constructor stub
	}
	
	public UserContext(ApartmentDetailsEntity apartmentDetailsEntity, 
						UserDetailsEntity userDetailsEntity, 
						Collection<? extends GrantedAuthority> authorities) {
		super(userDetailsEntity.getContactNo1(), userDetailsEntity.getPassword(), authorities);
		this.apartmentDetailsEntity = apartmentDetailsEntity;
		this.userDetailsEntity = userDetailsEntity;
		
		this.apartmentId = apartmentDetailsEntity.getId();
		this.userId = userDetailsEntity.getId();
	}
	
	public Long getSessionId() {
		if(sessionDetailsEntity != null) {
			return sessionDetailsEntity.getId();
		} else {
			throw new RuntimeException("Session not found on UserContext.");
		}
	}
	
	public String getUserFullName() {
		if(userDetailsEntity != null) {
			String firstName = userDetailsEntity.getFirstName() != null ? userDetailsEntity.getFirstName() : "";
			String lastName = userDetailsEntity.getLastName() != null ? userDetailsEntity.getLastName() : "";
			this.userFullName = firstName + " " + lastName;
			return this.userFullName.trim();
		} else {
			throw new RuntimeException("User not found on UserContext.");
		}
	}

}
