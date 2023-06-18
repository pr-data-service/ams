package com.drps.ams.service;

import java.util.List;

import javax.mail.MessagingException;

import com.drps.ams.bean.EmailProps;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EmailServiceDTO;
import com.drps.ams.dto.EmailSetupDetailsDTO;

public interface EmailService {

	ApiResponseEntity setupSaveOrUpdate(EmailSetupDetailsDTO emailSetupDetailsDTO);

	ApiResponseEntity getSetupByApartmentId();


	ApiResponseEntity getServiceByApartmentId();

	ApiResponseEntity serviceSaveOrUpdate(EmailServiceDTO dto) throws Exception;

	List<EmailProps> getEmailDetailsForDues();
}
