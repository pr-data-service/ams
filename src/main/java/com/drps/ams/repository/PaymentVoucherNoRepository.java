package com.drps.ams.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.PaymentVoucherNoEntity;

@Repository
public interface PaymentVoucherNoRepository  extends JpaRepository<PaymentVoucherNoEntity, Long>, CrudRepository<PaymentVoucherNoEntity, Long>,JpaSpecificationExecutor<PaymentVoucherNoEntity> {

	
	@Query("SELECT f FROM PaymentVoucherNoEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId")
	PaymentVoucherNoEntity get(Long apartmentId, Long sessionId);
}
