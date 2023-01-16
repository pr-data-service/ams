package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.entity.PaymentEntity;

@Repository
public interface ExpensesRepository extends JpaRepository<ExpensesEntity, Long>, CrudRepository<ExpensesEntity, Long>,JpaSpecificationExecutor<ExpensesEntity> {

	@Query("SELECT f FROM ExpensesEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId")
	List<ExpensesEntity> getAll(Long apartmentId, Long sessionId, Pageable pageable);
}
