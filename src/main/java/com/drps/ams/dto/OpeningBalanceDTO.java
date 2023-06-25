package com.drps.ams.dto;

import java.util.Date;

import javax.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.drps.ams.entity.OpeningBalanceEntity;
import com.drps.ams.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpeningBalanceDTO {

	private Long id;

	private Double inBankAccount;
	
	private Double cashInHand;
	
	private Long sessionId;
	
	private Long apartmentId;	
	
	private Long createdBy;	
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date createdDate;
	
	private Long modifiedBy;	
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date modifiedDate;
}
