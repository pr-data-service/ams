package com.drps.ams.service;

import com.drps.ams.dto.AccountTransactionDTO;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.OpeningBalanceDTO;

public interface AccountsService {

	ApiResponseEntity saveOrUpdate(AccountTransactionDTO dto);

	ApiResponseEntity get();

	ApiResponseEntity deleteById(Long id);

	AccountTransactionDTO saveOrUpdate(AccountTransactionDTO dto, Long apartmentId, Long userId);

	ApiResponseEntity openingBalanceSaveOrUpdate(OpeningBalanceDTO dto);

	ApiResponseEntity getOpeningBalance();

	ApiResponseEntity getPaymentInfo();

	ApiResponseEntity getExpenseInfo();

}
