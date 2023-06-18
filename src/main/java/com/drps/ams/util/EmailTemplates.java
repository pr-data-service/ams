package com.drps.ams.util;

//https://www.oodlestechnologies.com/dev-blog/sending-html-template-based-email-in-spring-boot-using-free-marker/

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.entity.join.FlatDetailsAndUserDetailsEntity;

import freemarker.template.Configuration;

public class EmailTemplates {
	
	public static String duesMail(Configuration fmConfiguration, FlatDetailsAndUserDetailsEntity faltAndUserEntity, List<PaymentDetailsDTO> list) {
		StringBuffer content = new StringBuffer();
		Map< String, Object > model = new HashMap<>();
        try {
        	model.put("firstName", faltAndUserEntity.getUserDetails().getFirstName());
        	model.put("lastName", faltAndUserEntity.getUserDetails().getLastName());
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate("dues-templates.flth"), model));
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return content.toString();
	}

}
