package com.drps.ams.service;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EmailSetupDetailsDTO;

public interface EmailService {

	ApiResponseEntity saveOrUpdate(EmailSetupDetailsDTO emailSetupDetailsDTO);

}
