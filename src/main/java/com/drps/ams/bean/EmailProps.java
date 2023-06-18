package com.drps.ams.bean;

import org.springframework.mail.javamail.JavaMailSender;

import lombok.Data;

@Data
public class EmailProps {
	
	JavaMailSender emailSender;
	
	String from;
	String to;
	String subject;
	String text;
	String pathToAttachment;
}
