package com.drps.ams.service;

import java.util.List;

import com.drps.ams.dto.ApartmentDetailsDTO;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ProjectDTO;
import com.drps.ams.dto.SessionDetailsDTO;

public interface ProjectService {

	ApiResponseEntity saveOrUpdate(ProjectDTO projectDTO);

	List<ApartmentDetailsDTO> getApartmentList();

	ApiResponseEntity getSessionListByUsername(String username);

	ApiResponseEntity getSessionListByApartmentId(Long apartmentId);
	
	ApiResponseEntity get() throws Exception;

	List<SessionDetailsDTO> getSessionList(Long apartmentId);

}
