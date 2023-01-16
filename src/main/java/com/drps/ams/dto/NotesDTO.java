package com.drps.ams.dto;

import java.util.Date;

import com.drps.ams.annotation.EntityMapping;
import com.drps.ams.entity.NotesEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
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
@EntityMapping(entity = NotesEntity.class)
public class NotesDTO {
	public Long id;
	public String noteType;
	public String title;
	public String noteText;
	public String parentObject;
	public Long parentRecordId;
	public Long sessionId;
	public Long apartmentId;
	public Long createdBy;	
	public String createdByName;
	@JsonFormat(pattern = "dd MMM, yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date createdDate;
	public Long modifiedBy;
	public String modifiedByName;
	@JsonFormat(pattern = "dd MMM, yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	public Date modifiedDate;	
}
