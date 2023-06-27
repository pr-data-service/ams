package com.drps.ams.dto;

import java.util.Date;

import com.drps.ams.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UserRolePermissionDTO {
	public Long id;
	public String object;
	public String role;
	public Boolean create;
	public Boolean view;
	public Boolean edit;
	public Boolean delete;
	
	
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
