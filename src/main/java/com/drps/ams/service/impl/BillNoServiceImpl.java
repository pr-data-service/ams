package com.drps.ams.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.entity.PaymentBillNoEntity;
import com.drps.ams.repository.PaymentBillNoRepository;
import com.drps.ams.service.BillNoService;
import com.drps.ams.util.Utils;

@Service
public class BillNoServiceImpl implements BillNoService {

	@Autowired
	PaymentBillNoRepository paymentBillNoRepository;
	
	@Override
	public String getPaymentBillNo() {
		UserContext userContext = Utils.getUserContext();
		Long startNo = Long.valueOf(1);
		String strNo = "";
		PaymentBillNoEntity paymentBillNoEntity = paymentBillNoRepository.get(userContext.getApartmentId(), userContext.getSessionId());
		if(paymentBillNoEntity != null) {
			strNo = String.format("%04d", paymentBillNoEntity.getBillNo());// Filling with zeroes & total digits four 
			paymentBillNoEntity.setBillNo(paymentBillNoEntity.getBillNo()+1);
			paymentBillNoRepository.save(paymentBillNoEntity);
		} else {
			strNo = String.format("%04d", startNo++);// Filling with zeroes & total digits four
			paymentBillNoEntity = new PaymentBillNoEntity();
			paymentBillNoEntity.setBillNo(startNo);
			paymentBillNoEntity.setSessionId(userContext.getSessionId());
			paymentBillNoEntity.setApartmentId(userContext.getApartmentId());
			paymentBillNoRepository.save(paymentBillNoEntity);
			
		}
		
		return strNo;
	}
}
