package com.drps.ams.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dbquery.DBQueryExecuter;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ExpenseItemsDTO;
import com.drps.ams.dto.ExpensesDTO;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.dto.PaymentDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.ExpenseItemsEntity;
import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
import com.drps.ams.entity.PaymentEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.exception.UserContextNotFoundException;
import com.drps.ams.repository.ExpenseItemsRepository;
import com.drps.ams.repository.ExpensesRepository;
import com.drps.ams.service.CommonService;
import com.drps.ams.service.ExpensesService;
import com.drps.ams.service.VoucherNoService;
import com.drps.ams.util.Utils;
import com.drps.pdf.ExpanseVoucherPDF;
import com.drps.pdf.PaymentReceiptPDF;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DynamicQuery;

import lombok.NonNull;

@Service
public class ExpensesServiceImpl implements ExpensesService {

	@Value("${file.download.path}")
	String FILE;
	
	@Autowired
	ExpensesRepository expensesRepository;
	
	@Autowired
	ExpenseItemsRepository expenseItemsRepository;

	@Autowired
	CommonService commonService;
	
	@Autowired
	VoucherNoService voucherNoService;
	
	@Autowired
	DBQueryExecuter dbQueryExecuter;
	
	@Override
	@Transactional
	public ApiResponseEntity saveOrUpdate(@NonNull ExpensesDTO expensesDTO) throws Exception {
		
		UserContext userContext = Utils.getUserContext();
		
		if(ApiConstants.LIST_OPTION_SELECT_VAL == expensesDTO.getEventId()) {
			expensesDTO.setEventId(null);
		}
		
		if(expensesDTO.getItems() != null && expensesDTO.getItems().size() > 0) {
			double mntnceTot = expensesDTO.getItems().stream().map(ExpenseItemsDTO::getAmount).collect(Collectors.summingDouble(Double::doubleValue));
			expensesDTO.setAmount(mntnceTot);
		} else {
			throw new RuntimeException("Item heads not found. Please add Item heads.");
		}
		
		ExpensesEntity expensesEntity = null;
		ExpenseItemsEntity expensesItemEntity = null;
		if(expensesDTO.getId() != null && expensesDTO.getId() > 0) {
			expensesEntity = expensesRepository.findById(expensesDTO.getId()).get();
			BeanUtils.copyProperties(expensesDTO, expensesEntity, Utils.getIgnoreEntityPropsOnUpdate(null));
			expensesEntity.setModifiedBy(userContext.getUserDetailsEntity().getId());
			expensesRepository.save(expensesEntity);
			
			expenseItemsRepository.deleteByExpenseId(userContext.getApartmentId(), userContext.getSessionId(), expensesDTO.getId());
		} else {
			expensesEntity = new ExpensesEntity();
			BeanUtils.copyProperties(expensesDTO, expensesEntity);
			expensesEntity.setVoucherNo(voucherNoService.getPaymentVoucherNo());
			expensesEntity.setCreatedBy(userContext.getUserId());
			expensesEntity.setModifiedBy(userContext.getUserId());
			expensesEntity.setSessionId(userContext.getSessionId());
			expensesEntity.setApartmentId(userContext.getApartmentId());
			expensesRepository.save(expensesEntity);
			
		}
		
		if(expensesDTO.getItems() != null) {
			for(ExpenseItemsDTO item : expensesDTO.getItems()) {
				if(item != null) {
					expensesItemEntity = new ExpenseItemsEntity();
					BeanUtils.copyProperties(item, expensesItemEntity);
					expensesItemEntity.setExpenseId(expensesEntity.getId());
					expensesItemEntity.setCreatedBy(userContext.getUserDetailsEntity().getId());
					expensesItemEntity.setModifiedBy(userContext.getUserDetailsEntity().getId());
					expensesItemEntity.setSessionId(userContext.getSessionId());
					expensesItemEntity.setApartmentId(userContext.getApartmentId());
					expenseItemsRepository.save(expensesItemEntity);
				}
			}
		}
			
		BeanUtils.copyProperties(expensesEntity, expensesDTO);	
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, expensesDTO);
	}

	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);		
		List<ExpensesDTO> rtnList = dbQueryExecuter.executeQuery(new QueryMaker(reqParamDto, ExpensesDTO.class));
		commonService.addUserDetailsToDTO(rtnList, ExpensesDTO.class);
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public ApiResponseEntity get() {
		UserContext userContext = Utils.getUserContext();
		
		List<ExpensesEntity> list = expensesRepository.findAll();

		List<ExpensesDTO> rtnList = Utils.convertList(list, ExpensesDTO.class);
		commonService.addUserDetailsToDTO(rtnList, ExpensesDTO.class);
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}

	@Override
	public ApiResponseEntity getById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			ExpensesEntity expensesEntity = expensesRepository.findById(id).get();
			if(expensesEntity != null) {
				ExpensesDTO expensesDTO = new ExpensesDTO();
				BeanUtils.copyProperties(expensesEntity, expensesDTO);
				
				List<ExpenseItemsEntity> itemsList = expenseItemsRepository.findByExpenseId(userContext.getApartmentId(), userContext.getSessionId(), id);
				expensesDTO.setItems(Utils.convertList(itemsList, ExpenseItemsDTO.class));
				
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, expensesDTO);
			} else {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	@Transactional
	public ApiResponseEntity deleteById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			expenseItemsRepository.deleteByExpenseId(userContext.getApartmentId(), userContext.getSessionId(), id);
			expensesRepository.deleteById(id);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity deleteAllById(List<Long> ids) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(ids != null && ids.size() > 0) {
			expensesRepository.deleteAllById(ids);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public File getFileToDownload(Long id) {
		UserContext userContext = Utils.getUserContext();
//		File file = new File("C:/Users/002ZX2744/Desktop/TEMP/pdf/expanse-voucher.pdf");
		File file = null;
		if(id != null && id > 0) {
			Map<String, Object> result = new HashMap<>();
			ExpensesEntity entity = expensesRepository.getById(id);
			if(entity != null) {
				List<ExpenseItemsEntity> expenseItemList = expenseItemsRepository.findByExpenseId(userContext.getApartmentId(), userContext.getSessionId(), id);
				
				
				ExpanseVoucherPDF pdf = new ExpanseVoucherPDF(FILE, entity, expenseItemList, result, null);
				file = pdf.getFile();
			}
		}
		
		return file;
	}
	
}
