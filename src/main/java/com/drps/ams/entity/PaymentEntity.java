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

import com.drps.ams.annotation.FKEntityFieldMapping;

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
@Table(name = "payment")
public class PaymentEntity {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "FLAT_ID")
	private Long flatId;
	
	@Column(name = "PAYMENT_MODE")
	private String paymentMode;
	
	@Column(name = "PAYMENT_MODE_REF")
	private String paymentModeRef;

	@Column(name = "AMOUNT")
	private Double amount;
	
	@Column(name = "PAYMENT_DATE")
	private Date paymentDate;

	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "IS_CANCELED")
	private Boolean isCanceled;
	
	@Column(name = "CANCEL_REMARKS")
	private String cancelRemarks;
	
	@Column(name = "BILL_NO")
	private String billNo;
	
	@Column(name = "PAYMENT_BY")
	private Long paymentBy;

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
