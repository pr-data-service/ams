package com.drps.ams.service;

import java.io.File;

import com.drps.ams.bean.PaymentInfo;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.PaymentOrVoucharCancelDTO;
import com.drps.ams.dto.PaymentSaveDTO;

public interface PaymentService {

	ApiResponseEntity getAddPageDetails(Long id);

	ApiResponseEntity getListView(String reqParams) throws Exception;

	ApiResponseEntity save(PaymentSaveDTO maintenanceSaveDTO) throws Exception;

	ApiResponseEntity deleteById(Long id) throws Exception;

	ApiResponseEntity get(Long id) throws Exception;
	
	ApiResponseEntity cancel(PaymentOrVoucharCancelDTO paymentCancel) throws Exception;

	File getFileToDownload(Long id);

	File downloadZip(String folderName) throws Exception;

	ApiResponseEntity getSlipByMonths();

	PaymentInfo getPaymentInfo();

}
