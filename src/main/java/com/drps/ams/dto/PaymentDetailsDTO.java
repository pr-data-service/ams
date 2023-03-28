package com.drps.ams.dto;

import java.util.Date;

import com.drps.ams.annotation.EntityFieldMapping;
import com.drps.ams.annotation.EntityMapping;
import com.drps.ams.annotation.FKEntityFieldMapping;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
import com.drps.ams.entity.PaymentEntity;
import com.drps.ams.entity.SessionDetailsEntity;
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
@EntityMapping(entity = PaymentDetailsEntity.class)
public class PaymentDetailsDTO {

	public Long id;
	
	public Long maintenanceId;
	
	@FKEntityFieldMapping(entity = FlatDetailsEntity.class, join = FKEntityFieldMapping.LEFT_JOIN)
	public Long flatId;
	
	@EntityFieldMapping(entity = FlatDetailsEntity.class, name = "flatNo")
	public String flatNo;
	
	public Double amount;
	
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date paymentDate;
	
	public Integer paymentMonth;
	public String paymentMonthName;
	public Integer paymentYear;
	public Integer paymentBy;
	public Long paymentTypeId;
	
	@FKEntityFieldMapping(entity = EventsEntity.class, join = FKEntityFieldMapping.LEFT_JOIN)
	public Long eventId;
	
	@EntityFieldMapping(entity = EventsEntity.class, name = "name")
	public String eventName;
	
	@FKEntityFieldMapping(entity = SessionDetailsEntity.class, join = FKEntityFieldMapping.LEFT_JOIN)
	public Long paymentForSessionId;
	
	@EntityFieldMapping(entity = SessionDetailsEntity.class, name = "name")
	public String paymentForSessionName;
	
	private Boolean isCanceled;
	public Long sessionId;
	public Long apartmentId;
	public Long createdBy;	
	public String createdByName;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date createdDate;
	
	public Long modifiedBy;	
	public String modifiedByName;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date modifiedDate;	
	
	public String ownersName;
	public String contactNo;
}
