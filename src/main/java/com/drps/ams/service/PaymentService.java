package com.drps.ams.service;

import java.io.File;
import java.util.Map;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.PaymentCancelDTO;
import com.drps.ams.dto.PaymentSaveDTO;

public interface PaymentService {

	ApiResponseEntity getAddPageDetails(Long id);

	ApiResponseEntity getListView(String reqParams) throws Exception;

	ApiResponseEntity save(PaymentSaveDTO maintenanceSaveDTO) throws Exception;

	ApiResponseEntity deleteById(Long id) throws Exception;

	ApiResponseEntity get(Long id) throws Exception;
	
	ApiResponseEntity cancel(PaymentCancelDTO paymentCancel) throws Exception;

	File getFileToDownload(Long id);

}
