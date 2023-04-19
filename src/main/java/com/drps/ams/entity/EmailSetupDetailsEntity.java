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
@Table(name = "email_setup_details")
public class EmailSetupDetailsEntity {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long id;
	
	@Column(name = "EMAIL")
	public String email;
	
	@Column(name = "PASSWORD")
	public String password;
	
	@Column(name = "IS_ACTIVE", columnDefinition = "true")
	public Boolean isActive;
	
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
