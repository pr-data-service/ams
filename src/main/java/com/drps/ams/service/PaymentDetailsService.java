package com.drps.ams.service;

import java.util.List;
import java.util.Map;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.entity.PaymentDetailsEntity;

public interface PaymentDetailsService {

	ApiResponseEntity getListView(String reqParams) throws Exception;

	void isDuplicatePaymentDetailsFound(List<PaymentDetailsDTO> list, Long flatId);

	ApiResponseEntity getDuesListView(String reqParams) throws Exception;

	List<PaymentDetailsDTO> getDuesList(Long flatId, Object[] arr);

	ApiResponseEntity saveLastPaymentDate(PaymentDetailsDTO paymentDetailsDto);

	PaymentDetailsEntity getLastPaymentForMaintenance(Long apartmentId, Long flatId);

	ApiResponseEntity getDuesListForAdvancePayment(Long flatId, int month, int year) throws Exception;

	Map<Long, List<PaymentDetailsDTO>> getDueListForEmailNotification(Long apartmentId, int excludesMonthCount);

	List<PaymentDetailsDTO> getDuesListForNotification(Long apartmentId, Long flatId, String flatNo);


}
