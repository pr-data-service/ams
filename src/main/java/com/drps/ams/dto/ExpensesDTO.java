package com.drps.ams.dto;

import java.util.Date;
import java.util.List;

import com.drps.ams.annotation.EntityFieldMapping;
import com.drps.ams.annotation.EntityMapping;
import com.drps.ams.annotation.FKEntityFieldMapping;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
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
@EntityMapping(entity = ExpensesEntity.class)
public class ExpensesDTO {
	
	public Long id;
	public String title;
	public String accountNo;
	public Double amount;
	public String description;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date expenseDate;
	
	public String voucherNo;
	public String paymentMode;
	
	@FKEntityFieldMapping(entity = EventsEntity.class, join = FKEntityFieldMapping.LEFT_JOIN)
	public Long eventId;
	
	@EntityFieldMapping(entity = EventsEntity.class, name = "name")
	public String eventName;
	private Boolean isCanceled;
	private String cancelRemarks;
	public Long apartmentId;
	public Long createdBy;
	public String createdByName;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date createdDate;
	
	public Long modifiedBy;	
	public String modifiedByName;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date modifiedDate;	
	
	public List<ExpenseItemsDTO> items;

}
