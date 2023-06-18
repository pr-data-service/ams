/**
 * 
 */
package com.drps.ams.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.EmailProps;
import com.drps.ams.bean.UserContext;
import com.drps.ams.cache.FlatDetailsCacheService;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EmailServiceDTO;
import com.drps.ams.dto.EmailSetupDetailsDTO;
import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.entity.ApartmentDetailsEntity;
import com.drps.ams.entity.EmailServiceEntity;
import com.drps.ams.entity.EmailSetupDetailsEntity;
import com.drps.ams.entity.join.FlatDetailsAndUserDetailsEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.repository.ApartmentDetailsRepository;
import com.drps.ams.repository.EmailServiceRepository;
import com.drps.ams.repository.EmailSetupDetailsRepository;
import com.drps.ams.service.EmailService;
import com.drps.ams.service.FlatDetailsService;
import com.drps.ams.service.PaymentDetailsService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DateUtils;
import com.drps.ams.util.EmailTemplates;
import com.drps.ams.util.Utils;

import freemarker.template.Configuration;

import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import lombok.NonNull;

/**
 * GMail App Password link
 * https://myaccount.google.com/apppasswords
 */
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
	EmailServiceRepository emailServiceRepository;
	
	@Autowired
	PaymentDetailsService paymentDetailsService;
	
	@Autowired
	FlatDetailsService flatDetailsService;

	@Autowired     
	Configuration fmConfiguration;
	
	@Autowired
	ApartmentDetailsRepository apartmentDetailsRepository;

	@Override
	public ApiResponseEntity setupSaveOrUpdate(@NonNull EmailSetupDetailsDTO emailSetupDetailsDTO) {

		UserContext userContext = Utils.getUserContext();
		if (isDuplicateRecord(emailSetupDetailsDTO)) {
			throw new DuplicateRecordException(
					ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
		}

		EmailSetupDetailsEntity emailSetupDetailsEntity = null;
		if (emailSetupDetailsDTO.getId() != null && emailSetupDetailsDTO.getId() > 0) {
			emailSetupDetailsEntity = emailSetupDetailsRepository.findById(emailSetupDetailsDTO.getId()).get();
			if (emailSetupDetailsEntity == null) {
				throw new NoRecordFoundException(
						ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			} else {
				BeanUtils.copyProperties(emailSetupDetailsDTO, emailSetupDetailsEntity,
						Utils.getIgnoreEntityPropsOnUpdate(new String[] {}));
				emailSetupDetailsEntity.setModifiedBy(userContext.getUserId());
			}

		} else {
			emailSetupDetailsEntity = new EmailSetupDetailsEntity();
			BeanUtils.copyProperties(emailSetupDetailsDTO, emailSetupDetailsEntity);
			emailSetupDetailsEntity.setApartmentId(userContext.getApartmentId());
			emailSetupDetailsEntity.setCreatedBy(userContext.getUserId());
			emailSetupDetailsEntity.setModifiedBy(userContext.getUserId());
		}

		emailSetupDetailsEntity = emailSetupDetailsRepository.save(emailSetupDetailsEntity);

		BeanUtils.copyProperties(emailSetupDetailsEntity, emailSetupDetailsDTO);

		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, emailSetupDetailsDTO);
	}

	private boolean isDuplicateRecord(EmailSetupDetailsDTO emailSetupDetailsDTO) {
		UserContext userContext = Utils.getUserContext();

		List<EmailSetupDetailsEntity> list = emailSetupDetailsRepository.findByEmail(userContext.getApartmentId(),
				emailSetupDetailsDTO.getEmail());
		if (list != null && emailSetupDetailsDTO.getId() != null && emailSetupDetailsDTO.getId() > 0) {
			list = list.stream().filter(f -> f.getId() != emailSetupDetailsDTO.getId()).collect(Collectors.toList());
		}

		return list != null && list.size() > 0 ? true : false;
	}

	@Override
	public ApiResponseEntity getSetupByApartmentId() {
		UserContext userContext = Utils.getUserContext();
		EmailSetupDetailsDTO emailSetupDetailsDto = new EmailSetupDetailsDTO();

		EmailSetupDetailsEntity emailSetupDetailsEntity = null;
		if (userContext.getApartmentId() != null && userContext.getApartmentId() > 0) {

			emailSetupDetailsEntity = emailSetupDetailsRepository.findByApartmentId(userContext.getApartmentId());
			BeanUtils.copyProperties(emailSetupDetailsEntity, emailSetupDetailsDto);
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, emailSetupDetailsDto);
	}
	
	@Override
	public ApiResponseEntity serviceSaveOrUpdate(EmailServiceDTO dto) throws Exception {
		UserContext userContext = Utils.getUserContext();
			if(dto.getId() != null && dto.getId() > 0) {
				EmailServiceEntity entity = emailServiceRepository.findById(dto.getId()).get();
				if (entity != null) {
					entity.setModifiedBy(userContext.getUserId());
					entity.setIsActive(dto.getIsActive());
					entity = emailServiceRepository.save(entity);
					dto.setId(entity.getId());
					dto.setIsActive(entity.getIsActive());
					dto.setType(entity.getType());
					dto.setName(entity.getName());
				}
				else {
					throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
				}

			}else {	
				EmailServiceEntity entity = new EmailServiceEntity();
				BeanUtils.copyProperties(dto, entity);
				entity.setApartmentId(userContext.getApartmentId());
				entity.setCreatedBy(userContext.getUserId());
				entity = emailServiceRepository.save(entity);
				BeanUtils.copyProperties(entity, dto);
			}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dto) ;
	}


	@Override
	public ApiResponseEntity getServiceByApartmentId() {
		UserContext userContext = Utils.getUserContext();

		List<EmailServiceEntity> list = emailServiceRepository.getByApartmentId(userContext.getApartmentId());
		List<EmailServiceDTO> dtos = new ArrayList<EmailServiceDTO>();
		for(EmailServiceEntity entity: list) {
			EmailServiceDTO dto = new EmailServiceDTO();
			dto.setId(entity.getId());
			dto.setIsActive(entity.getIsActive());
			dto.setType(entity.getType());
			dto.setName(entity.getName());
			dtos.add(dto);
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dtos);
	}

	@Override
	public List<EmailProps> getEmailDetailsForDues() {
		List<EmailProps> senderList = new ArrayList<>(); 
		
		
		FlatDetailsAndUserDetailsEntity flatDetailsAndUserDetails = null;
		List<ApartmentDetailsEntity> aprtList = apartmentDetailsRepository.findAll();
		aprtList = aprtList.stream().filter( f -> f.getIsActive()).collect(Collectors.toList());
		
		for(ApartmentDetailsEntity aprt : aprtList) {
			
			EmailSetupDetailsEntity emailSetupDetailsEntity = emailSetupDetailsRepository.findByApartmentId(aprt.getId());
			if(emailSetupDetailsEntity != null && !StringUtils.isBlank(emailSetupDetailsEntity.getEmail())) {
				
				EmailServiceEntity duesEmailServiceEntity = emailServiceRepository.getNotificationDues(aprt.getId());
				if(duesEmailServiceEntity != null && duesEmailServiceEntity.getIsActive()) {
					JavaMailSender javaMailSender = getJavaMailSender(emailSetupDetailsEntity);
					
					Map<Long, List<PaymentDetailsDTO>> flatMap = paymentDetailsService.getDueListForEmailNotification(aprt.getId(), 1);
					
					if(!Objects.isNull(flatMap) && !flatMap.isEmpty()) {
						List<PaymentDetailsDTO> list = null;
						for(Long flatId : flatMap.keySet()) {
							flatDetailsAndUserDetails = flatDetailsService.findFlatDetailsAndUserDetailsById(aprt.getId(), flatId);
							list = flatMap.get(flatId);
							
							if(!Objects.isNull(flatDetailsAndUserDetails.getUserDetails().getEmailId())) {
								String template = EmailTemplates.duesMail(fmConfiguration, flatDetailsAndUserDetails, list);
								
								EmailProps emailProps = new EmailProps();
								emailProps.setEmailSender(javaMailSender);
								emailProps.setSubject("Maintenance Dues as on "+ DateUtils.dateToString(null, "MM, YYYY"));
								emailProps.setFrom(emailSetupDetailsEntity.getEmail());
								emailProps.setTo(flatDetailsAndUserDetails.getUserDetails().getEmailId());
								emailProps.setText(template);
								senderList.add(emailProps);
							}
						}
					}
					
				}
			}
		}
		
		return senderList;
	}
	
	/**
	 * @implNote Required to set GMail App Password link
	 * https://myaccount.google.com/apppasswords
	 */
	public JavaMailSender getJavaMailSender(EmailSetupDetailsEntity emailSetupDetailsEntity) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        
        mailSender.setUsername(emailSetupDetailsEntity.getEmail());
        mailSender.setPassword(emailSetupDetailsEntity.getPassword());
        
        Properties props = mailSender.getJavaMailProperties();
        //props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.debug", "true");
        
        return mailSender;
    }

}
