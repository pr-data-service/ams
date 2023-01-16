package com.drps.ams.dto;

import java.util.Date;

import com.drps.ams.entity.ExpenseHeadEntity;
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
public class ExpenseHeadDTO {

	public Long id;	
	public Long eventId;
	public String title;
	public String description;
	public Long sessionId;
	public Long apartmentId;	
	public Long createdBy;	
	public String createdByName;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date createdDate;
	public Long modifiedBy;
	public String modifiedByName;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date modifiedDate;	
}
