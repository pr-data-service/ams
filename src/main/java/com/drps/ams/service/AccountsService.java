package com.drps.ams.service;

import com.drps.ams.dto.AccountTransactionDTO;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.OpeningBalanceDTO;

public interface AccountsService {

	ApiResponseEntity saveOrUpdate(AccountTransactionDTO dto);

	ApiResponseEntity get();

	ApiResponseEntity deleteById(Long id);

	ApiResponseEntity openingBalanceSaveOrUpdate(OpeningBalanceDTO dto);

	ApiResponseEntity getOpeningBalance();

	ApiResponseEntity getPaymentInfo();

	ApiResponseEntity getExpenseInfo();

}
