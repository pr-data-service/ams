package com.drps.ams.service;

import java.util.List;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ProjectDTO;
import com.drps.ams.dto.SessionDetailsDTO;

public interface ProjectService {

	ApiResponseEntity saveOrUpdate(ProjectDTO projectDTO);

	List<SessionDetailsDTO> getSessionList(String username);

	ApiResponseEntity get() throws Exception;

}
