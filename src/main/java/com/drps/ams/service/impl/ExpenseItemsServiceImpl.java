package com.drps.ams.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dbquery.DBQueryExecuter;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ExpenseItemsDTO;
import com.drps.ams.dto.ExpensesDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.ExpenseItemsEntity;
import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.exception.UserContextNotFoundException;
import com.drps.ams.repository.ExpenseItemsRepository;
import com.drps.ams.service.CommonService;
import com.drps.ams.service.ExpenseItemsService;
import com.drps.ams.util.Utils;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DynamicQuery;

@Service
public class ExpenseItemsServiceImpl implements ExpenseItemsService {

	@Autowired
	ExpenseItemsRepository expenseItemsRepository;
	
	@Autowired
	DBQueryExecuter dbQueryExecuter;
	
	@Autowired
	CommonService commonService;
	
	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		
		
		List<ExpenseItemsDTO> rtnList = dbQueryExecuter.executeQuery(new QueryMaker(reqParamDto, ExpenseItemsDTO.class));
		commonService.addUserDetailsToDTO(rtnList, ExpenseItemsDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}

}
