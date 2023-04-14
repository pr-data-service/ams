package com.drps.ams.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.dto.PaymentDTO;
import com.drps.ams.entity.PaymentEntity;

@Repository
public interface PaymentRepository  extends JpaRepository<PaymentEntity, Long>, 
												CrudRepository<PaymentEntity, Long>,
												JpaSpecificationExecutor<PaymentEntity> {
	

	@Query("SELECT f FROM PaymentEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId")
	List<PaymentEntity> getAll(Long apartmentId, Long sessionId, Pageable pageable);
	
	@Query("SELECT f FROM PaymentEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId"
			+ " AND f.paymentDate BETWEEN :startDate AND :endDate")
	public List<PaymentEntity> getTodaysPaymentList(Long apartmentId, Long sessionId, Date startDate, Date endDate);
	
	
	@Query("SELECT p, f.flatNo, CONCAT(u.firstName, ' ', u.lastName) as createdBy, CONCAT(u2.firstName, ' ', u2.lastName) as paymentBy  FROM PaymentEntity p, FlatDetailsEntity f, UserDetailsEntity u, UserDetailsEntity u2 WHERE f.id = p.flatId AND p.apartmentId = :apartmentId AND p.sessionId = :sessionId"
			+" AND u.id = p.createdBy AND u2.id = p.paymentBy AND p.paymentDate BETWEEN :startDate AND :endDate")
	public List<Object[]> getMonthlyPaymentList (Long apartmentId, Long sessionId, Date startDate, Date endDate);
}
