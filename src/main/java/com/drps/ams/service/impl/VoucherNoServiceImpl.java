package com.drps.ams.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.entity.PaymentVoucherNoEntity;
import com.drps.ams.repository.PaymentBillNoRepository;
import com.drps.ams.repository.PaymentVoucherNoRepository;
import com.drps.ams.service.VoucherNoService;
import com.drps.ams.util.Utils;

@Service
public class VoucherNoServiceImpl implements VoucherNoService {

	@Autowired
	PaymentVoucherNoRepository paymentVoucherNoRepository;
	
	@Override
	public String getPaymentVoucherNo() {
		UserContext userContext = Utils.getUserContext();
		Long startNo = Long.valueOf(1);
		String strNo = "";
		PaymentVoucherNoEntity paymentVoucherNoEntity = paymentVoucherNoRepository.get(userContext.getApartmentId(), userContext.getSessionId());
		if(paymentVoucherNoEntity != null) {
			strNo = String.format("%04d", paymentVoucherNoEntity.getVoucherNo());// Filling with zeroes & total digits four 
			paymentVoucherNoEntity.setVoucherNo(paymentVoucherNoEntity.getVoucherNo()+1);
			paymentVoucherNoRepository.save(paymentVoucherNoEntity);
		} else {
			strNo = String.format("%04d", startNo++);// Filling with zeroes & total digits four
			paymentVoucherNoEntity = new PaymentVoucherNoEntity();
			paymentVoucherNoEntity.setVoucherNo(startNo);
			paymentVoucherNoEntity.setSessionId(userContext.getSessionId());
			paymentVoucherNoEntity.setApartmentId(userContext.getApartmentId());
			paymentVoucherNoRepository.save(paymentVoucherNoEntity);
			
		}
		
		return strNo;
	}
}
