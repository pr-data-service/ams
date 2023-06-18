package com.drps.ams;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.drps.ams.util.ApiConstants;

@SpringBootApplication
@EnableScheduling
@EnableCaching
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
