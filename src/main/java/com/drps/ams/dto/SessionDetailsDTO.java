package com.drps.ams.dto;

import java.util.Date;

import com.drps.ams.entity.SessionDetailsEntity;
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
public class SessionDetailsDTO {
	private Long id;
	private String name;
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date fromDate;
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date toDate;
	private Boolean isActive;
	private Long apartmentId;
	private Integer createdBy;	
	private Date createdDate;
	private Integer modifiedBy;	
	private Date modifiedDate;	
}
