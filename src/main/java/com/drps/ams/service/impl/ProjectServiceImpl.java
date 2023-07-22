package com.drps.ams.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApartmentDetailsDTO;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EventsDTO;
import com.drps.ams.dto.ProjectDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.dto.SessionDetailsDTO;
import com.drps.ams.dto.UserDetailsDTO;
import com.drps.ams.entity.ApartmentDetailsEntity;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.exception.UserContextNotFoundException;
import com.drps.ams.repository.ApartmentDetailsRepository;
import com.drps.ams.repository.SessionDetailsRepository;
import com.drps.ams.repository.UserDetailsRepository;
import com.drps.ams.security.JwtTokenUtil;
import com.drps.ams.service.ProjectService;
import com.drps.ams.service.SessionDetailsService;
import com.drps.ams.service.UserDetailsService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.Utils;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.NonNull;

@Service
public class ProjectServiceImpl implements ProjectService {
	
	private static final  Logger logger = LogManager.getLogger(ProjectServiceImpl.class);
	
	@Autowired
	ApartmentDetailsRepository apartmentDetailsRepository;
	
	@Autowired
	UserDetailsRepository userDetailsRepository;
	
	@Autowired
	SessionDetailsRepository sessionDetailsRepository;
	
	@Autowired
	private JwtUserDetailsServiceImpl jwtUserDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	SessionDetailsService sessionDetailsService;
	
	@Transactional
	@Override
	public ApiResponseEntity saveOrUpdate(@NonNull ProjectDTO projectDTO) {
		
		ApartmentDetailsDTO appartmentDto = projectDTO.getApartmentDetails();
		UserDetailsDTO userDto = projectDTO.getUserDetails();
		SessionDetailsDTO sessionDto = projectDTO.getSessionDetails();
		
		ApartmentDetailsEntity appEntity = null;
		UserDetailsEntity userEntity = null;
		
		if(Objects.isNull(appartmentDto)) {
			throw new RuntimeException("Appartment Data not found!");
		} else {
			appEntity = new ApartmentDetailsEntity();
			BeanUtils.copyProperties(appartmentDto, appEntity);
			appEntity.setIsActive(true);
			appEntity.setCreatedBy(ApiConstants.SYSTEM_USER_ID);
			appEntity.setModifiedBy(ApiConstants.SYSTEM_USER_ID);
			apartmentDetailsRepository.save(appEntity);
		}
		
		if(Objects.isNull(userDto) || appEntity == null || appEntity.getId() <= 0) {
			throw new RuntimeException("User Data not found!");
		} else {
			
			if(userDetailsService.isDuplicateRecord(userDto)) {
				throw new DuplicateRecordException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
			}
			
			userEntity = new UserDetailsEntity();
			BeanUtils.copyProperties(userDto, userEntity);
			userEntity.setIsActive(true);
			userEntity.setPassword(Utils.generateRandomPassword(8));
			userEntity.setApartmentId(appEntity.getId());
			userEntity.setCreatedBy(ApiConstants.SYSTEM_USER_ID);
			userEntity.setModifiedBy(ApiConstants.SYSTEM_USER_ID);
			userDetailsRepository.save(userEntity);

			appEntity.setUserId(userEntity.getId());
			apartmentDetailsRepository.save(appEntity);
		}

		if(Objects.isNull(sessionDto) || appEntity == null || appEntity.getId() <= 0) {
			throw new RuntimeException("Session Data not found!");
		} else {
			sessionDto.setIsActive(true);
			sessionDetailsService.saveOrUpdate(sessionDto, appEntity.getId(), ApiConstants.SYSTEM_USER_ID);
		}
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, userEntity.getPassword());
	}
	
	@Override
	public List<ApartmentDetailsDTO> getApartmentList() {
		
		List<ApartmentDetailsEntity> list = apartmentDetailsRepository.findAll();
		return Utils.convertList(list, ApartmentDetailsDTO.class);
	}
	
	@Override
	public ApiResponseEntity getSessionListByUsername(String username) {
				
		UserContext userContext = null;
		if(username != null) {
			userContext = this.jwtUserDetailsService.loadUserByUsername(username);
		}
		
		if(userContext == null) {
			throw new UserContextNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_USER_CONTEXT_NOT_FOUND_EXCEPTION));
		}
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, getSessionList(userContext.getApartmentId()));
	}
	
	@Override
	public ApiResponseEntity getSessionListByApartmentId(Long apartmentId) {
				
		UserContext userContext = Utils.getUserContext();
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, getSessionList(apartmentId));
	}
	
	@Override
	public List<SessionDetailsDTO> getSessionList(Long apartmentId) {
		List<SessionDetailsEntity> list = sessionDetailsRepository.findByApartmentId(apartmentId);

		List<SessionDetailsDTO> rtnList = new ArrayList<>();
		if (list != null) {
			SessionDetailsDTO sessionDTO = null;
			for (SessionDetailsEntity sessionEntity : list) {
				sessionDTO = new SessionDetailsDTO();
				BeanUtils.copyProperties(sessionEntity, sessionDTO);
				rtnList.add(sessionDTO);
			}
		}
		return rtnList;
	}
	
	@Override
	public ApiResponseEntity get() throws Exception {
		UserContext userContext = Utils.getUserContext();
		ProjectDTO dto = new ProjectDTO();
		
		ApartmentDetailsEntity entity = apartmentDetailsRepository.getById(userContext.getApartmentId());
		if(entity != null) {
			ApartmentDetailsDTO aprtDto = new ApartmentDetailsDTO();
			BeanUtils.copyProperties(entity, aprtDto);
			dto.setApartmentDetails(aprtDto);
		} 
		
		SessionDetailsEntity sessEnty = sessionDetailsRepository.getById(userContext.getSessionId());
		if(sessEnty != null) {
			SessionDetailsDTO sesDto = new SessionDetailsDTO();
			BeanUtils.copyProperties(sessEnty, sesDto);
			dto.setSessionDetails(sesDto);
		} 
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dto);
	}

}
