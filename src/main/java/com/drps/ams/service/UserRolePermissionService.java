package com.drps.ams.service;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.UserRolePermissionDTO;

import lombok.NonNull;

public interface UserRolePermissionService {

	public ApiResponseEntity saveOrUpdateUserRolePermission(@NonNull UserRolePermissionDTO dto);

	public ApiResponseEntity getUserRolePermission();

	public ApiResponseEntity getUserRolePermission(String object, String role);
}
