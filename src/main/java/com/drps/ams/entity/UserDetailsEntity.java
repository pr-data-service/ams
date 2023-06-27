package com.drps.ams.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "user_details")
public class UserDetailsEntity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;	
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "ROLE")
	private String role;
	
	@Column(name = "LOGIN_ID")
	private String loginId;	
	
	@Column(name = "FIRST_NAME")
	private String firstName;	
	
	@Column(name = "MIDDLE_NAME")
	private String middleName;	
	
	@Column(name = "LAST_NAME")
	private String lastName;	
	
	@Column(name = "ADHAR_CARD_NO")
	private String adharCardNo;	
	
	@Column(name = "USER_ADDRESS")
	private String userAddress;
	
	@Column(name = "CONTACT_NO_1")
	private String contactNo1;	
	
	@Column(name = "CONTACT_NO_2")
	private String contactNo2;
	
	@Column(name = "EMAIL_ID")
	private String emailId;	
	
	@Column(name = "PASSWORD")
	private String password;	
	
	@Column(name = "APARTMENT_ID")
	private Long apartmentId;	
	
	@Column(name = "IS_DELETED", columnDefinition = "boolean default false")
	private Boolean isDeleted;
	
	@Column(name = "IS_ACTIVE", columnDefinition = "boolean default true")
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
