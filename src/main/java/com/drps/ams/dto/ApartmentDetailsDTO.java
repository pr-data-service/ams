package com.drps.ams.dto;

import java.util.Date;

import com.drps.ams.annotation.EntityMapping;
import com.drps.ams.entity.ApartmentDetailsEntity;
import com.drps.ams.entity.EventsEntity;
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
@EntityMapping(entity = ApartmentDetailsEntity.class)
public class ApartmentDetailsDTO {

	private Long id;
	private String name;
	private String address1;
	private String address2;
	private String pinCode;
	private String state;
	private Boolean isActive;
	private Long userId;
	private Integer createdBy;	
	private Date createdDate;
	private Integer modifiedBy;	
	private Date modifiedDate;	

}
