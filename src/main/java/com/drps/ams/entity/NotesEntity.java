package com.drps.ams.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Entity
@Table(name = "notes")
public class NotesEntity {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long id;
	
	@Column(name = "TITLE")
	public String title;
	
	@Column(name = "NOTE_TYPE")
	public String noteType;
	
	@Column(name = "NOTE_TEXT")
	public String noteText;
	
	@Column(name = "PARENT_OBJECT")
	public String parentObject;
	
	@Column(name = "PARENT_RECORD_ID")
	public Long parentRecordId;
	
	@Column(name = "SESSION_ID")
	public Long sessionId;
	
	@Column(name = "APARTMENT_ID")
	public Long apartmentId;
	
	@Column(name = "CREATED_BY")
	public Long createdBy;	
	
	@Column(name = "CREATED_DATE")
	@CreationTimestamp
	public Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	public Long modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	@UpdateTimestamp
	public Date modifiedDate;	
}
