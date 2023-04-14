package com.drps.ams.service;

import javax.mail.MessagingException;

import com.drps.ams.bean.EmailProps;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EmailSetupDetailsDTO;

public interface EmailService {

	ApiResponseEntity saveOrUpdate(EmailSetupDetailsDTO emailSetupDetailsDTO);

	ApiResponseEntity getByApartmentId();

	void sendSimpleMessage(EmailProps emailProps) throws MessagingException;
}
