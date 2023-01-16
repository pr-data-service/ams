package com.drps.ams.service;

import java.util.List;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EventsDTO;

import lombok.NonNull;

public interface EventsService {

	ApiResponseEntity saveOrUpdate(@NonNull EventsDTO eventsDTO) throws Exception;

	ApiResponseEntity getListView(String reqParams) throws Exception;

	ApiResponseEntity get(String reqParams);

	ApiResponseEntity getById(Long id) throws Exception;

	ApiResponseEntity activeInActive(Long id, boolean isActive) throws Exception;

	ApiResponseEntity deleteAllById(List<Long> ids) throws Exception;

	ApiResponseEntity getByExpenseId(Long expenseId) throws Exception;
}
