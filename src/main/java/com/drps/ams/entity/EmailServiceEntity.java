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
@Table(name = "email_service")
public class EmailServiceEntity {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "IS_ACTIVE", columnDefinition = "true")
	private Boolean isActive;

	@Column(name = "APARTMENT_ID")
	private Long apartmentId;

	@Column(name = "CREATED_BY")
	private Long createdBy;	

	@Column(name = "CREATED_DATE")
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private Long modifiedBy;

	@Column(name = "MODIFIED_DATE")
	@UpdateTimestamp
	private Date modifiedDate;	
}
