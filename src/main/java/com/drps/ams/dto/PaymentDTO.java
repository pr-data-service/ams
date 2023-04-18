package com.drps.ams.dto;

import java.util.Date;

import javax.persistence.Column;

import com.drps.ams.annotation.EntityMapping;
import com.drps.ams.annotation.FKEntityFieldMapping;
import com.drps.ams.annotation.EntityFieldMapping;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.PaymentEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
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
@EntityMapping(entity = PaymentEntity.class)
public class PaymentDTO {

	public Long id;
	
	@FKEntityFieldMapping(entity = FlatDetailsEntity.class, join = FKEntityFieldMapping.LEFT_JOIN)
	public Long flatId;
	
	@EntityFieldMapping(entity = FlatDetailsEntity.class, name = "flatNo")
	public String flatNo;
	
	public String paymentMode;
	public String paymentModeRef;
	public Double amount;
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date paymentDate;
	public String remarks;
	private Boolean isCanceled;
	private String cancelRemarks;
	private String billNo;
	
	public Long apartmentId;
	@FKEntityFieldMapping(entity = UserDetailsEntity.class, join = FKEntityFieldMapping.LEFT_JOIN)
	public Long paymentBy;
	
	@EntityFieldMapping(entity = UserDetailsEntity.class, name = "CONCAT(firstName, lastName)")
	public String paymentByName;
	public Long createdBy;	
	public String createdByName;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date createdDate;
	
	public Long modifiedBy;	
	public String modifiedByName;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date modifiedDate;	
	
	
}
