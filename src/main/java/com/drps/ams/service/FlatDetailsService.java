package com.drps.ams.service;

import java.util.List;
import java.util.Map;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.join.FlatDetailsAndUserDetailsEntity;

import lombok.NonNull;

public interface FlatDetailsService {

	ApiResponseEntity saveOrUpdate(@NonNull FlatDetailsDTO flatDetailsDTO) throws Exception;

	ApiResponseEntity getListView(String reqParams) throws Exception;

	ApiResponseEntity getById(Long id) throws Exception;

	ApiResponseEntity deleteById(Long id) throws Exception;

	ApiResponseEntity deleteAllById(List<Long> ids) throws Exception;

	ApiResponseEntity get();

	Map<Long, FlatDetailsAndUserDetailsEntity> getFlatDetailsAndUserDetailsMap(Long apartmentId);

	FlatDetailsAndUserDetailsEntity findFlatDetailsAndUserDetailsById(Long aprtmentId, Long flatId);

	FlatDetailsEntity findById(long aprtmentId, long flatId);

}
