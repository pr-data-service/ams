package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.ExpenseHeadEntity;
import com.drps.ams.entity.FlatDetailsEntity;

@Repository
public interface ExpenseHeadRepository extends JpaRepository<ExpenseHeadEntity, Long>, CrudRepository<ExpenseHeadEntity, Long>,JpaSpecificationExecutor<ExpenseHeadEntity> {

	@Query("SELECT f FROM ExpenseHeadEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId AND f.title = :title")
	public List<ExpenseHeadEntity> findByTitle(Long apartmentId, Long sessionId, String title);

	@Query("SELECT f FROM ExpenseHeadEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId")
	public List<ExpenseHeadEntity> getAll(Long apartmentId, Long sessionId);
}
