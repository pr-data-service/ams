package com.drps.ams.dto;

import java.util.Date;

import javax.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.drps.ams.annotation.EntityFieldMapping;
import com.drps.ams.annotation.EntityMapping;
import com.drps.ams.annotation.FKEntityFieldMapping;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.MaintenanceEntity;
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
@EntityMapping(entity = MaintenanceEntity.class)
public class MaintenanceDTO {

	public Long id;
	public Double amount;
	
	@FKEntityFieldMapping(entity = FlatDetailsEntity.class, join = FKEntityFieldMapping.LEFT_JOIN)
	public Long flatId;	
	
	@EntityFieldMapping(entity = FlatDetailsEntity.class, name = "flatNo")
	public String flatNo;
	
	@FKEntityFieldMapping(entity = SessionDetailsEntity.class, join = FKEntityFieldMapping.LEFT_JOIN)
	public Long sessionId;
	
	@EntityFieldMapping(entity = SessionDetailsEntity.class, name = "name")
	public String sessionName;
	
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
