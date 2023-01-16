package com.drps.ams.service;

import java.util.List;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.FlatDetailsDTO;

import lombok.NonNull;

public interface FlatDetailsService {

	ApiResponseEntity saveOrUpdate(@NonNull FlatDetailsDTO flatDetailsDTO) throws Exception;

	ApiResponseEntity getListView(String reqParams) throws Exception;

	ApiResponseEntity getById(Long id) throws Exception;

	ApiResponseEntity deleteById(Long id) throws Exception;

	ApiResponseEntity deleteAllById(List<Long> ids) throws Exception;

	ApiResponseEntity get();

}
