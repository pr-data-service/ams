package com.drps.ams.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.ExpensesEntity;

@Repository
public interface ExpensesRepository extends JpaRepository<ExpensesEntity, Long>, CrudRepository<ExpensesEntity, Long>,JpaSpecificationExecutor<ExpensesEntity> {

	@Query("SELECT f FROM ExpensesEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId")
	List<ExpensesEntity> getAll(Long apartmentId, Long sessionId, Pageable pageable);
	
	@Query("SELECT f FROM ExpensesEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId")
	List<ExpensesEntity> getAll(Long apartmentId, Long sessionId);
	
	@Query("SELECT e, CONCAT(u.firstName, ' ', u.lastName) as createdBy, ev.name" 
			+" FROM ExpensesEntity e" 
			+" INNER JOIN UserDetailsEntity u ON u.id = e.createdBy" 
			+" LEFT JOIN EventsEntity ev ON ev.id = e.eventId" 
			+" WHERE e.apartmentId = :apartmentId" 
			+" AND e.sessionId = :sessionId" 
			+" AND (ev.id = e.eventId OR e.eventId IS NULL)" 
			+" AND e.expenseDate BETWEEN :startDate AND :endDate")
	public List<Object[]> getMonthlyExpensesList (Long apartmentId, Long sessionId, Date startDate, Date endDate);
	
	@Query("SELECT (count(e) > 0) FROM ExpensesEntity e WHERE e.eventId = :eventId")
	public boolean isEventExist(Long eventId);
}
