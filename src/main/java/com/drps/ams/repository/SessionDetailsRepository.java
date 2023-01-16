package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.SessionDetailsEntity;

@Repository
public interface SessionDetailsRepository extends JpaRepository<SessionDetailsEntity, Long>,
																CrudRepository<SessionDetailsEntity, Long>, 
																JpaSpecificationExecutor<SessionDetailsEntity> {
	
	@Query("SELECT s FROM SessionDetailsEntity s WHERE s.apartmentId = :apartmentId")
	public List<SessionDetailsEntity> findByApartmentId(Long apartmentId);
	
	@Query("SELECT s FROM SessionDetailsEntity s WHERE s.apartmentId = :apartmentId AND s.id = :sessionId")
	public SessionDetailsEntity findBySessionId(Long apartmentId, Long sessionId);
	
	@Query("SELECT f FROM SessionDetailsEntity f WHERE f.apartmentId = :apartmentId")
	List<SessionDetailsEntity> getAll(Long apartmentId);
	
	@Query("SELECT f FROM SessionDetailsEntity f WHERE f.apartmentId = :apartmentId AND f.id IN (:sessionIdList)")
	List<SessionDetailsEntity> getSessionDetailsList(Long apartmentId, List<Long> sessionIdList);
	
	@Query("SELECT s.name FROM SessionDetailsEntity s WHERE s.apartmentId = :apartmentId AND s.id = :sessionId")
	public String getSessionName(Long apartmentId, Long sessionId);
	
	@Query("SELECT f FROM SessionDetailsEntity f WHERE f.apartmentId = :apartmentId AND f.name = :name")
	List<SessionDetailsEntity> findByName(Long apartmentId, String name);

	@Query("SELECT f FROM SessionDetailsEntity f WHERE f.apartmentId = :apartmentId ORDER BY f.toDate DESC")
	List<SessionDetailsEntity> findSessionOrderByToDate(Long apartmentId);
}
 