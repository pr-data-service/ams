package com.drps.ams.scheduler;
//https://reflectoring.io/spring-scheduler/

import java.util.List;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.drps.ams.bean.EmailProps;
import com.drps.ams.email.EmailProcessor;
import com.drps.ams.service.EmailService;
import com.drps.ams.util.DateUtils;

@Component
public class EmailScheduler {
	
	private static final Logger logger = LogManager.getLogger(EmailScheduler.class);
	
	@Value("${email-scheduler.active}")
	Boolean isEmailSchedulerActive;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	EmailProcessor emailProcessor;

	
	/*
	 * With this configuration, Spring will schedule the annotated method to 
	 * run at 10:15 AM on the 15th day of every month.
	 * 
	 * cron = "* * * * * *"
	 * second (0-59), 
	 * minute (0 - 59), 
	 * hour (0 - 23), day of the month (1 - 31), month (1 - 12) (or JAN-DEC), day of the week (0 - 7)
	 * 
	 * 
	 * Schedulded at: 0 Second, 0 Minutes, 10 Hour, 1th Day of month
	 * 
	 * Every month 1st day, 10:00:00
	 */
	//@Scheduled(cron = "0 0 10 1 * *")
	@Scheduled(cron = "0 6 16 10 * *")
	//@Scheduled(cron = "${email-scheduler.interval-in-cron}")
	public void sendEmailsForDues() {
	 
	    System.out.println("schedule tasks using cron jobs - " + DateUtils.dateTimeToString(null));
	    if(isEmailSchedulerActive) {
	    	List<EmailProps> list = emailService.getEmailDetailsForDues();
		    for(EmailProps props : list) {
		    	try {
					emailProcessor.sendMimeMessage(props);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e);
				}
		    }
	    }
	    
	}
}
