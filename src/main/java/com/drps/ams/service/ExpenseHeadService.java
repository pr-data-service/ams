package com.drps.ams.service;

import java.util.List;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ExpenseHeadDTO;

import lombok.NonNull;

public interface ExpenseHeadService {

	ApiResponseEntity saveOrUpdate(@NonNull ExpenseHeadDTO expenseHeadDTO) throws Exception;
	ApiResponseEntity getListView(String reqParams) throws Exception;
	ApiResponseEntity get();
	ApiResponseEntity getById(Long id) throws Exception;
	ApiResponseEntity deleteById(Long id) throws Exception;
	ApiResponseEntity deleteAllById(List<Long> ids) throws Exception;

}
