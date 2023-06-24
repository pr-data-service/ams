package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.AccountTransactionEntity;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransactionEntity, Long>,
		CrudRepository<AccountTransactionEntity, Long>, JpaSpecificationExecutor<AccountTransactionEntity> {

	@Query("SELECT f FROM AccountTransactionEntity f WHERE f.apartmentId = :apartmentId")
	List<AccountTransactionEntity> getAll(Long apartmentId);
	
	@Query("SELECT f FROM AccountTransactionEntity f WHERE f.apartmentId = :apartmentId AND f.refNo = :refNo")
	List<AccountTransactionEntity> findByRefNo(Long apartmentId, String refNo);
}
