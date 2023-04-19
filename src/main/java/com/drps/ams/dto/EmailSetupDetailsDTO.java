package com.drps.ams.dto;

import java.util.Date;

import lombok.Data;

@Data
public class EmailSetupDetailsDTO {

	public Long id;
	public String email;
	public String password;
	public Boolean isActive;
	public Long apartmentId;
	public Long createdBy;	
	public Date createdDate;
	public Long modifiedBy;
	public Date modifiedDate;	
}
