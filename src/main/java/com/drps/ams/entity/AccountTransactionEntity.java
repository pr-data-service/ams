package com.drps.ams.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "account_transaction")
public class AccountTransactionEntity {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "REF_NO")
	private String refNo;

	@Column(name = "AMOUNT")
	private Double amount;
	
	@Column(name = "TRANS_DATE")
	private Date transDate;
	
	@Column(name = "SESSION_ID")
	private Long sessionId;	
	
	@Column(name = "APARTMENT_ID")
	private Long apartmentId;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;	
	
	@Column(name = "CREATED_DATE")
	@CreationTimestamp
	private Date createdDate;
}
