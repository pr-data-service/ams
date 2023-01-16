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
@Table(name = "flat_details")
public class FlatDetailsEntity {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long id;
	
	@Column(name = "FLAT_NO")
	public String flatNo;

	@Column(name = "BLOCK")
	public String block;

	@Column(name = "FLAT_SIZE_SQFT")
	public Integer flatSizeSqft;

	@Column(name = "NO_OF_ROOMS")
	public Integer noOfRooms;

	@Column(name = "FLOOR_NO")
	public Integer floorNo;

	@Column(name = "FLAT_TYPE")
	public String flatType;
	
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
