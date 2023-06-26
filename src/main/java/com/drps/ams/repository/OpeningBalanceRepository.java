package com.drps.ams.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.OpeningBalanceEntity;

@Repository
public interface OpeningBalanceRepository extends JpaRepository<OpeningBalanceEntity, Long>,
		CrudRepository<OpeningBalanceEntity, Long>, JpaSpecificationExecutor<OpeningBalanceEntity> {

	@Query("SELECT f FROM OpeningBalanceEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId")
	OpeningBalanceEntity get(Long apartmentId, Long sessionId);
}
