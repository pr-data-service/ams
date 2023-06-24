package com.drps.ams.service;

import com.drps.ams.dto.AccountTransactionDTO;
import com.drps.ams.dto.ApiResponseEntity;

public interface AccountsService {

	ApiResponseEntity saveOrUpdate(AccountTransactionDTO dto);

	ApiResponseEntity get();

	ApiResponseEntity deleteById(Long id);

	AccountTransactionDTO saveOrUpdate(AccountTransactionDTO dto, Long apartmentId, Long userId);

}
