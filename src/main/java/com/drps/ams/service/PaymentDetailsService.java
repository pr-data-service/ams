package com.drps.ams.service;

import java.util.List;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.entity.PaymentDetailsEntity;

public interface PaymentDetailsService {

	ApiResponseEntity getListView(String reqParams) throws Exception;

	void isDuplicatePaymentDetailsFound(List<PaymentDetailsDTO> list, Long flatId);

	ApiResponseEntity getDuesListView(String reqParams) throws Exception;

	List<PaymentDetailsDTO> getDuesList(Long flatId, Object[] arr);

	ApiResponseEntity saveLastPaymentDate(PaymentDetailsDTO paymentDetailsDto);

	PaymentDetailsEntity getLastPaymentForMaintenance(Long flatId);

	ApiResponseEntity getDuesListForAdvancePayment(Long flatId, int month, int year) throws Exception;


}
