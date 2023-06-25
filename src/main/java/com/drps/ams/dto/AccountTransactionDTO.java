package com.drps.ams.dto;

import java.util.Date;

import com.drps.ams.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransactionDTO {
	private Long id;
	private String type;
	private String refNo;
	private Double amount;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date transDate;
	private Long sessionId;	
	private Long apartmentId;
	private Long createdBy;	
	private Date createdDate;
}
