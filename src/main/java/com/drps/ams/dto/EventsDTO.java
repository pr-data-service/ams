package com.drps.ams.dto;

import java.util.Date;

import com.drps.ams.annotation.EntityMapping;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityMapping(entity = EventsEntity.class)
public class EventsDTO {
	public Long id;
	public String name;
	public String description;
	public Double amountPerFlat;
	public Double targetAmount;
	public Boolean isActive;
	public Long apartmentId;
	public Long createdBy;	
	public String createdByName;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date createdDate;
	public Long modifiedBy;	
	public String modifiedByName;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date modifiedDate;	
	public Double dueAmount;
}
