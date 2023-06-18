package com.drps.ams.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.ExpenseItemsEntity;
import com.drps.ams.entity.PaymentDetailsEntity;

@Repository
public interface ExpenseItemsRepository extends JpaRepository<ExpenseItemsEntity, Long>, CrudRepository<ExpenseItemsEntity, Long>,JpaSpecificationExecutor<ExpenseItemsEntity> {

	@Modifying(clearAutomatically = true)
	@Query("DELETE ExpenseItemsEntity m WHERE m.apartmentId = :apartmentId AND m.sessionId = :sessionId AND m.expenseId =:expenseId")
	void deleteByExpenseId(Long apartmentId, Long sessionId, Long expenseId);
	
	@Query("SELECT m FROM ExpenseItemsEntity m WHERE m.apartmentId = :apartmentId AND m.sessionId = :sessionId AND m.expenseId = :expenseId")
	public List<ExpenseItemsEntity> findByExpenseId(Long apartmentId, Long sessionId, Long expenseId);
	
	@Query("SELECT m FROM ExpenseItemsEntity m WHERE m.apartmentId = :apartmentId AND m.sessionId = :sessionId" 
			+ " AND m.createdDate BETWEEN :startDt AND :endDt")
	public List<ExpenseItemsEntity> getMonthlyExpenseItemsList(Long apartmentId, Long sessionId, Date startDt, Date endDt);
}
