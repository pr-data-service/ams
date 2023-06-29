package com.drps.ams.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.drps.ams.entity.PaymentDetailsEntity;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetailsEntity, Long>, CrudRepository<PaymentDetailsEntity, Long>,JpaSpecificationExecutor<PaymentDetailsEntity> {

	@Query(value = "SELECT m FROM PaymentDetailsEntity m WHERE m.apartmentId = :apartmentId AND m.sessionId = :sessionId AND m.flatId = :flatId ORDER BY m.paymentYear DESC, m.paymentMonth DESC", nativeQuery = true)
	public PaymentDetailsEntity getLastPaymentByFlatId(Long apartmentId, Long sessionId, Integer flatId);
	
	@Query("SELECT m FROM PaymentDetailsEntity m WHERE m.apartmentId = :apartmentId AND m.sessionId = :sessionId AND m.flatId = :flatId")
	public List<PaymentDetailsEntity> findByFlatId(Long apartmentId, Long sessionId, Long flatId);
	
	@Query("SELECT m FROM PaymentDetailsEntity m WHERE m.apartmentId = :apartmentId AND m.flatId = :flatId AND (m.isCanceled IS NULL OR m.isCanceled = 0) AND m.paymentMonth IS NULL AND m.paymentYear IS NULL")
	public List<PaymentDetailsEntity> findByFlatIdForEventDueList(Long apartmentId, Long flatId);
	
	@Query("SELECT m FROM PaymentDetailsEntity m WHERE m.apartmentId = :apartmentId AND m.flatId = :flatId")
	public List<PaymentDetailsEntity> findByFlatId(Long apartmentId, Long flatId);
	
	@Query("SELECT m FROM PaymentDetailsEntity m WHERE m.apartmentId = :apartmentId AND m.flatId = :flatId AND (m.isCanceled IS NULL OR m.isCanceled = 0)")
	public List<PaymentDetailsEntity> findByFlatIdForDueList(Long apartmentId, Long flatId);
	
	@Modifying(clearAutomatically = true)
	@Query("DELETE PaymentDetailsEntity m WHERE m.apartmentId = :apartmentId AND m.sessionId = :sessionId AND m.paymentId =:paymentId")
	void deletePaymentDetailsByMaintenanceId(Long apartmentId, Long sessionId, Long paymentId);
	
	@Query("SELECT m FROM PaymentDetailsEntity m WHERE m.apartmentId = :apartmentId AND m.sessionId = :sessionId AND m.paymentId = :paymentId")
	public List<PaymentDetailsEntity> findByPaymentId(Long apartmentId, Long sessionId, Long paymentId);
	
	@Query("SELECT m FROM PaymentDetailsEntity m WHERE m.apartmentId = :apartmentId AND m.paymentForSessionId = :paymentForSessionId AND m.flatId = :flatId"
			+ " AND m.paymentMonth > 0 AND m.paymentYear > 0")
	public List<PaymentDetailsEntity> getPaymentForSessionId(Long apartmentId, Long paymentForSessionId, Long flatId);
	
	@Query("SELECT m FROM PaymentDetailsEntity m WHERE m.apartmentId = :apartmentId AND m.flatId = :flatId"
			+ " AND m.paymentMonth > 0 AND m.paymentYear > 0")
	public List<PaymentDetailsEntity> getMaintenanceList(Long apartmentId, Long flatId);
	
	@Query("SELECT m FROM PaymentDetailsEntity m WHERE m.apartmentId = :apartmentId AND m.paymentForSessionId = :paymentForSessionId AND m.flatId = :flatId"
			+ " AND m.paymentMonth > 0 AND m.paymentYear > 0 AND m.amount = :amount")
	public List<PaymentDetailsEntity> getPaymentForSessionId(Long apartmentId, Long paymentForSessionId, Long flatId, double amount);
	
	
	@Query("SELECT m FROM PaymentDetailsEntity m WHERE m.apartmentId = :apartmentId AND m.sessionId = :sessionId"
			+ " AND m.paymentMonth > 0 AND m.paymentYear > 0")
	public List<PaymentDetailsEntity> getMaintenanceDetailsList(Long apartmentId, Long sessionId);
	
	@Query("SELECT m FROM PaymentDetailsEntity m WHERE m.apartmentId = :apartmentId AND (m.isCanceled IS NULL OR m.isCanceled = 0)"
			+ " AND m.eventId != 1")
	public List<PaymentDetailsEntity> getEventPaymentList(Long apartmentId);
	
	@Query("SELECT f.id, f.flatNo, SUM(m.amount) FROM PaymentDetailsEntity m, FlatDetailsEntity f WHERE m.flatId = f.id AND m.apartmentId = :apartmentId"
			+ " AND m.eventId = :eventId AND (m.isCanceled IS NULL OR m.isCanceled = 0) GROUP BY m.flatId")
	public List<Object[]> getEventPaymentList(Long apartmentId, Long eventId);
	
	@Query("SELECT p, e.name FROM PaymentDetailsEntity p, EventsEntity e WHERE e.id = p.eventId AND p.apartmentId = :apartmentId AND p.sessionId = :sessionId AND p.paymentDate BETWEEN :startDt AND :endDt")
	public List<Object[]> getMonthlyPaymentDetailsList(Long apartmentId, Long sessionId, Date startDt, Date endDt);
	
	@Query("SELECT (count(p) > 0) FROM PaymentDetailsEntity p WHERE p.eventId = :eventId")
	public boolean isEventExist(Long eventId);
}
