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
@Table(name = "link_flat_details_user_details")
public class LinkFlatDetailsAndUserDetailsEntity {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "FLAT_ID")
	private Long flatId;

	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "LINK_DATE")
	private Date linkDate;	
	
	@Column(name = "UNLINK_DATE")
	private Date unlinkDate;	
	
	@Column(name = "APARTMENT_ID")
	private Long apartmentId;	
	
	@Column(name = "IS_DELETED", columnDefinition = "false")
	private Boolean isDeleted;
	
	@Column(name = "IS_ACTIVE", columnDefinition = "true")
	private Boolean isActive;
	
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
