package com.drps.ams.service;

import java.util.List;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.UserDetailsDTO;
import com.drps.ams.dto.UserPasswordDTO;

public interface UserDetailsService {

	public ApiResponseEntity saveOrUpdate(UserDetailsDTO userDetailsDTO) throws Exception;
	public ApiResponseEntity getListView(String reqParams) throws Exception;
	public ApiResponseEntity getById(Long id) throws Exception;
	public ApiResponseEntity deleteById(Long id) throws Exception;
	public ApiResponseEntity deleteAllById(List<Long> ids) throws Exception;
	public ApiResponseEntity get();
	public boolean isDuplicateRecord(UserDetailsDTO userDetailsDTO);
	ApiResponseEntity getByFlatId(Long flatId) throws Exception;
	ApiResponseEntity getLoggedInUder() throws Exception;
	ApiResponseEntity updatePassword(UserPasswordDTO userPasswordDto) throws Exception;
}
