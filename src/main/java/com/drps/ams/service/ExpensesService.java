package com.drps.ams.service;

import java.io.File;
import java.util.List;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ExpensesDTO;
import com.drps.ams.dto.PaymentOrVoucharCancelDTO;

import lombok.NonNull;

public interface ExpensesService {

	ApiResponseEntity saveOrUpdate(@NonNull ExpensesDTO expensesDTO) throws Exception;

	ApiResponseEntity getListView(String reqParams) throws Exception;

	ApiResponseEntity get();

	ApiResponseEntity getById(Long id) throws Exception;

	ApiResponseEntity deleteById(Long id) throws Exception;

	ApiResponseEntity deleteAllById(List<Long> ids) throws Exception;

	File getFileToDownload(Long id);

	ApiResponseEntity cancel(@NonNull PaymentOrVoucharCancelDTO cancel) throws Exception;

	ApiResponseEntity getVoucherByMonths();

	File downloadZip(String folderName) throws Exception;

}
