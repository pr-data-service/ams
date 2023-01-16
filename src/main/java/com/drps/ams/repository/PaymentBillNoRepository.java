package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.PaymentBillNoEntity;

@Repository
public interface PaymentBillNoRepository  extends JpaRepository<PaymentBillNoEntity, Long>, CrudRepository<PaymentBillNoEntity, Long>,JpaSpecificationExecutor<PaymentBillNoEntity> {

	
	@Query("SELECT f FROM PaymentBillNoEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId")
	PaymentBillNoEntity get(Long apartmentId, Long sessionId);
}
