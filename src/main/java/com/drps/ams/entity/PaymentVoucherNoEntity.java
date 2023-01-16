package com.drps.ams.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "payment_voucher_no")
public class PaymentVoucherNoEntity {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long id;
	
	@Column(name = "VOUCHER_NO")
	public Long voucherNo;
	
	@Column(name = "SESSION_ID")
	public Long sessionId;
	
	@Column(name = "APARTMENT_ID")
	public Long apartmentId;
	
	@Column(name = "MODIFIED_DATE")
	@UpdateTimestamp
	public Date modifiedDate;
}
