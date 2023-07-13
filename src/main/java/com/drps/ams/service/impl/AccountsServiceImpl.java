package com.drps.ams.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.ExpenseInfo;
import com.drps.ams.bean.PaymentInfo;
import com.drps.ams.bean.UserContext;
import com.drps.ams.dto.AccountTransactionDTO;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.OpeningBalanceDTO;
import com.drps.ams.entity.AccountTransactionEntity;
import com.drps.ams.entity.OpeningBalanceEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.repository.AccountTransactionRepository;
import com.drps.ams.repository.OpeningBalanceRepository;
import com.drps.ams.repository.UserDetailsRepository;
import com.drps.ams.service.AccountsService;
import com.drps.ams.service.ExpensesService;
import com.drps.ams.service.PaymentService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.ParameterVerifier;
import com.drps.ams.util.Utils;

@Service
public class AccountsServiceImpl implements AccountsService {
	
	private static final  Logger logger = LogManager.getLogger(AccountsServiceImpl.class);
	
	@Autowired
	AccountTransactionRepository accountTransactionRepository;
	
	@Autowired
	OpeningBalanceRepository openingBalanceRepository;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	ExpensesService expensesService;
	
	@Autowired
	UserDetailsRepository userDetailsRepository;
	
	@Override
	public ApiResponseEntity saveOrUpdate(AccountTransactionDTO dto) {
		UserContext userContext = Utils.getUserContext();
		
		if(isDuplicateRecord(dto, userContext.getApartmentId())) {
			throw new DuplicateRecordException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
		} 
		
		AccountTransactionEntity entity = new AccountTransactionEntity();
		BeanUtils.copyProperties(dto, entity);
		entity.setApartmentId(userContext.getApartmentId());
		entity.setSessionId(userContext.getSessionId());
		entity.setCreatedBy(userContext.getUserId());
		accountTransactionRepository.save(entity);
		
		BeanUtils.copyProperties(entity, dto);	
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dto);
	}
	
	@Override
	public ApiResponseEntity get() {
		UserContext userContext = Utils.getUserContext();
		
		List<AccountTransactionEntity> list = accountTransactionRepository.getAll(userContext.getApartmentId());
		List<AccountTransactionDTO> rtnList = Utils.convertList(list, AccountTransactionDTO.class);
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}

	@Override
	public ApiResponseEntity deleteById(Long id) {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			accountTransactionRepository.deleteById(id);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	private boolean isDuplicateRecord(AccountTransactionDTO dto, Long apartmentId) {
		
		List<AccountTransactionEntity> list = accountTransactionRepository.findByRefNo(apartmentId, dto.getRefNo());
		if(list != null && dto.getId() != null && dto.getId() > 0) {
			list = list.stream().filter( f -> f.getId() != dto.getId()).collect(Collectors.toList());
		}
		
		return list != null && list.size() > 0 ? true : false;
	}
	
	@Override
	public ApiResponseEntity openingBalanceSaveOrUpdate(OpeningBalanceDTO dto) {
		UserContext userContext = Utils.getUserContext();
		
		OpeningBalanceEntity entity = new OpeningBalanceEntity();
		if(dto.getId() != null && dto.getId() > 0) {
			entity = openingBalanceRepository.findById(dto.getId()).orElseThrow();
			BeanUtils.copyProperties(dto, entity, Utils.getIgnoreEntityPropsOnUpdate(null));
			entity.setModifiedBy(userContext.getUserDetailsEntity().getId());
		} else {
			entity = new OpeningBalanceEntity();
			BeanUtils.copyProperties(dto, entity);
			entity.setCreatedBy(userContext.getUserId());
			entity.setApartmentId(userContext.getApartmentId());
			entity.setSessionId(userContext.getSessionId());
		}
		
		openingBalanceRepository.save(entity);
		BeanUtils.copyProperties(entity, dto);
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dto);
	}

	@Override
	public ApiResponseEntity getOpeningBalance() {
		UserContext userContext = Utils.getUserContext();
		OpeningBalanceEntity entity = openingBalanceRepository.get(userContext.getApartmentId(), userContext.getSessionId());
		
		OpeningBalanceDTO dto = new OpeningBalanceDTO();
		if(entity != null) {
			BeanUtils.copyProperties(entity, dto);
			
			if(!Objects.isNull(entity.getCreatedBy())) {
				UserDetailsEntity createdBy = userDetailsRepository.findById(entity.getCreatedBy()).orElse(null);
				dto.setCreatedByName(Utils.getUserFullName(createdBy));
			}
			
			if(!Objects.isNull(entity.getModifiedBy())) {
				UserDetailsEntity modifiedBy = userDetailsRepository.findById(entity.getModifiedBy()).orElse(null);
				dto.setModifiedByName(Utils.getUserFullName(modifiedBy));
			}
			
			dto.setInBankAccount( ParameterVerifier.getDouble(dto.getInBankAccount()));
			dto.setCashInHand( ParameterVerifier.getDouble(dto.getCashInHand()));
		} else {
			dto.setCreatedByName(StringUtils.EMPTY);
			dto.setModifiedByName(StringUtils.EMPTY);
			
			dto.setInBankAccount(Double.valueOf(0));
			dto.setCashInHand(Double.valueOf(0));
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dto);
	}
	
	@Override
	public ApiResponseEntity getPaymentInfo() {
		UserContext userContext = Utils.getUserContext();
		PaymentInfo paymentInfo = paymentService.getPaymentInfo();
		if(paymentInfo == null) {
			paymentInfo = new PaymentInfo();
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, paymentInfo);
	}
	
	@Override
	public ApiResponseEntity getExpenseInfo() {
		UserContext userContext = Utils.getUserContext();
		ExpenseInfo expenseInfo = expensesService.getExpenseInfo();
		if(expenseInfo == null) {
			expenseInfo = new ExpenseInfo();
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, expenseInfo);
	}

}
