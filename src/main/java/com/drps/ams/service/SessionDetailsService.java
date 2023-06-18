package com.drps.ams.service;

import java.util.List;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.dto.SessionDetailsDTO;

public interface SessionDetailsService {

	List<SessionDetailsDTO> getSessionList(String username);

	ApiResponseEntity get();

	void addSessionIdAndMaintenanceOnList(List<PaymentDetailsDTO> list, Long flatId);

	void addPaymentForSessionNameOnList(List<PaymentDetailsDTO> list);

	ApiResponseEntity saveOrUpdate(SessionDetailsDTO dto);

	ApiResponseEntity deleteById(Long id);

	SessionDetailsDTO saveOrUpdate(SessionDetailsDTO dto, Long apartmentId, Long userId);

	void addSessionIdAndMaintenanceOnList(Long apartmentId, List<PaymentDetailsDTO> list, Long flatId);

}
