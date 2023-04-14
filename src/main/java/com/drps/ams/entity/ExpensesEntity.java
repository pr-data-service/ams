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
@Table(name = "EXPENSES")
public class ExpensesEntity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "ACCOUNT_NO")
	private String accountNo;
	
	@Column(name = "AMOUNT")
	private Double amount;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "EXPENSE_DATE")
	private Date expenseDate;
	
	@Column(name = "IS_CANCELED")
	private Boolean isCanceled;
	
	@Column(name = "CANCEL_REMARKS")
	private String cancelRemarks;
	
	@Column(name = "VOUCHER_NO")
	private String voucherNo;
	
	@Column(name = "PAYMENT_MODE")
	public String paymentMode;
	
	@Column(name = "EVENT_ID")
	private Long eventId;
	
	@Column(name = "SESSION_ID")
	private Long sessionId;
	
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
