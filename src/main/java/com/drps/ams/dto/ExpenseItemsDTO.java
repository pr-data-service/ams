package com.drps.ams.dto;

import java.util.Date;

import javax.persistence.Column;

import com.drps.ams.annotation.EntityMapping;
import com.drps.ams.entity.ExpenseItemsEntity;
import com.drps.ams.entity.ExpensesEntity;
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
@EntityMapping(entity = ExpenseItemsEntity.class)
public class ExpenseItemsDTO {
	
	public Long id;
	public Long expenseId;
	public String itemHead;
	public Double amount;
	public Long apartmentId;
	public Long createdBy;	
	public String createdByName;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date createdDate;
	public Long modifiedBy;
	public String modifiedByName;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date modifiedDate;	
}
