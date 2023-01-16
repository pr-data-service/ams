package com.drps.ams.service;

import com.drps.ams.dto.ApiResponseEntity;

public interface ExpenseItemsService {
	ApiResponseEntity getListView(String reqParams) throws Exception;
}
