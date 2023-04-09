package com.drps.ams;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.drps.ams.util.ApiConstants;

@SpringBootApplication
public class AmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmsApplication.class, args);
	}

	@PostConstruct
	void started() {
		
		// Application default time zone setting here.
		TimeZone.setDefault(TimeZone.getTimeZone(ApiConstants.STRING_TIMEZONE_ID_IST));
	}

}
