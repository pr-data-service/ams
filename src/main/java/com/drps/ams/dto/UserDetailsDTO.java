package com.drps.ams.dto;

import java.util.Date;

import com.drps.ams.annotation.EntityMapping;
import com.drps.ams.entity.UserDetailsEntity;
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
@EntityMapping(entity = UserDetailsEntity.class)
public class UserDetailsDTO {

	private Long id;	
	
	private String type;
	
	private String role;
	
	private String loginId;	
	
	private String firstName;	
	
	private String middleName;	
	
	private String lastName;	
	
	private String adharCardNo;	
	
	private String userAddress;	
	
	private String contactNo1;	
	
	private String contactNo2;	
	
	private String emailId;
	
	private Long apartmentId;	
	
	private Boolean isDeleted;	
	
	private Boolean isActive;
	
	private Long createdBy;	
	private String createdByName;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date createdDate;
	
	private Long modifiedBy;	
	private String modifiedByName;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date modifiedDate;	
}
