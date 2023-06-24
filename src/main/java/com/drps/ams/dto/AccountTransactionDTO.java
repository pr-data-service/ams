package com.drps.ams.dto;

import java.util.Date;

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
	private Date transDate;
	private Long sessionId;	
	private Long apartmentId;
	private Long createdBy;	
	private Date createdDate;
}
