package com.drps.ams.service;

import java.util.List;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.PaymentDetailsDTO;

public interface PaymentDetailsService {

	ApiResponseEntity getListView(String reqParams) throws Exception;

	void isDuplicatePaymentDetailsFound(List<PaymentDetailsDTO> list, Long flatId);

	ApiResponseEntity getDuesListView(String reqParams) throws Exception;

	List<PaymentDetailsDTO> getDuesList(Long flatId, Object[] arr);

	ApiResponseEntity save(PaymentDetailsDTO paymentDetailsDto);


}
