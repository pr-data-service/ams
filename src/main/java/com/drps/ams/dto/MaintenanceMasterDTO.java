package com.drps.ams.dto;

import java.util.Date;

import com.drps.ams.entity.MaintenanceMasterEntity;
import com.drps.ams.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceMasterDTO {

	private Long id;

	private Double amount;
	
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date fromDate;
	
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date toDate;
	
	private Boolean isActive;
	private String strActiveInActive;
	
	private Long createdBy;
	private String strCreatedBy;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date createdDate;
	
	private Long modifiedBy;
	private String strModifiedBy;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date modifiedDate;
}
