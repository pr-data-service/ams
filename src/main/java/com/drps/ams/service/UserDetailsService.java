package com.drps.ams.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.UserDetailsDTO;
import com.drps.ams.dto.UserPasswordDTO;
import com.drps.ams.dto.UserRoleUpdateDTO;
import com.drps.ams.entity.UserDetailsEntity;

import lombok.NonNull;

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
	public ApiResponseEntity uploadSignature(MultipartFile file) throws Exception;
	void getSignature(HttpServletRequest req, HttpServletResponse res);
	ApiResponseEntity getOnlyUserList();
	ApiResponseEntity updateOnlyUserRole(UserRoleUpdateDTO dto);
	ApiResponseEntity removeUserRole(@NonNull Long id);
	Map<String, List<String>> getUserPermissions(Long apartmentId, String role);
	UserDetailsDTO findAnyOneUserByRole(String role);
	List<UserDetailsDTO> findUserListByRole(String role);
}
