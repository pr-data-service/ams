package com.drps.ams.email;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.EmailProps;


@Service
public class EmailProcessor {
	
	private static final Logger logger = LogManager.getLogger(EmailProcessor.class);

	/**
	 * Required to set GMail App Password link
	 * https://myaccount.google.com/apppasswords
	 */
	public void sendMimeMessage(EmailProps emailProps) throws MessagingException {
		JavaMailSender emailSender = emailProps.getEmailSender();
		MimeMessage message = emailSender.createMimeMessage();

		// set mediaType   
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,   
             StandardCharsets.UTF_8.name());   


//		helper.setFrom("pradyut.april@gmail.com");
		helper.setFrom(emailProps.getFrom());
		helper.setTo(emailProps.getTo());
		helper.setTo("pradyut.track4@gmail.com");//"rajdeep.ttsl@gmail.com"
		helper.setSubject(emailProps.getSubject());
		helper.setText(emailProps.getText(), true);

//		FileSystemResource file = new FileSystemResource(new File(emailProps.getPathToAttachment()));
//		helper.addAttachment("Invoice.pdf", file);

		emailSender.send(message);
	}
	
	
}
