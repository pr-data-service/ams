/**
 * 
 */
package com.drps.ams.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.EmailProps;
import com.drps.ams.bean.UserContext;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EmailSetupDetailsDTO;
import com.drps.ams.entity.EmailSetupDetailsEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.repository.EmailSetupDetailsRepository;
import com.drps.ams.service.EmailService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.Utils;
import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import lombok.NonNull;

/**
 * @author prady
 *
 */
@Service
public class EmailServiceImpl implements EmailService {
	
	private static final Logger logger = LogManager.getLogger(EmailServiceImpl.class);
	
	@Autowired
	EmailSetupDetailsRepository emailSetupDetailsRepository;
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Override
	public ApiResponseEntity saveOrUpdate(@NonNull EmailSetupDetailsDTO emailSetupDetailsDTO) {
			
		UserContext userContext = Utils.getUserContext();
		if(isDuplicateRecord(emailSetupDetailsDTO)) {
			throw new DuplicateRecordException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
		}
		
		EmailSetupDetailsEntity emailSetupDetailsEntity = null;
		if(emailSetupDetailsDTO.getId() != null && emailSetupDetailsDTO.getId() > 0) {
			emailSetupDetailsEntity = emailSetupDetailsRepository.findById(emailSetupDetailsDTO.getId()).get();
			if(emailSetupDetailsEntity == null) {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			} else {
				BeanUtils.copyProperties(emailSetupDetailsDTO, emailSetupDetailsEntity, Utils.getIgnoreEntityPropsOnUpdate(new String[] {}));
				emailSetupDetailsEntity.setModifiedBy(userContext.getUserId());
			}
			
		} else {
			emailSetupDetailsEntity = new EmailSetupDetailsEntity();
			BeanUtils.copyProperties(emailSetupDetailsDTO, emailSetupDetailsEntity);
			emailSetupDetailsEntity.setApartmentId(userContext.getApartmentId());
			emailSetupDetailsEntity.setCreatedBy(userContext.getUserId());
			emailSetupDetailsEntity.setModifiedBy(userContext.getUserId());
		}
		
			
		emailSetupDetailsRepository.save(emailSetupDetailsEntity);
			
		BeanUtils.copyProperties(emailSetupDetailsEntity, emailSetupDetailsDTO);	
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, emailSetupDetailsDTO);
	}

	private boolean isDuplicateRecord(EmailSetupDetailsDTO emailSetupDetailsDTO) {
		UserContext userContext = Utils.getUserContext();
		
		List<EmailSetupDetailsEntity> list = emailSetupDetailsRepository.findByEmail(userContext.getApartmentId(), emailSetupDetailsDTO.getEmail());
		if(list != null && emailSetupDetailsDTO.getId() != null && emailSetupDetailsDTO.getId() > 0) {
			list = list.stream().filter( f -> f.getId() != emailSetupDetailsDTO.getId()).collect(Collectors.toList());
		}
		
		return list != null && list.size() > 0 ? true : false;
	}
	
	@Override
	public ApiResponseEntity getByApartmentId() {
		UserContext userContext = Utils.getUserContext();
		EmailSetupDetailsDTO emailSetupDetailsDto = new EmailSetupDetailsDTO();
		
		EmailSetupDetailsEntity emailSetupDetailsEntity = null;
		if (userContext.getApartmentId() != null && userContext.getApartmentId() > 0) {
			
			emailSetupDetailsEntity = emailSetupDetailsRepository.findByApartmentId(userContext.getApartmentId());
			BeanUtils.copyProperties(emailSetupDetailsEntity,emailSetupDetailsDto);
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, emailSetupDetailsDto);
	}
	

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("pradyut.bca15@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}

	@Override
	public void sendSimpleMessage(EmailProps emailProps)
			throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom("pradyut.april@gmail.com");
		helper.setTo(emailProps.getTo());
		helper.setSubject(emailProps.getSubject());
		helper.setText(emailProps.getText());

		FileSystemResource file = new FileSystemResource(new File(emailProps.getPathToAttachment()));
		helper.addAttachment("Invoice", file);

		emailSender.send(message);
	}
	
}
