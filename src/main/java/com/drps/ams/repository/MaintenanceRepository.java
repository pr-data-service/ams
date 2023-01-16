package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.MaintenanceEntity;

@Repository
public interface MaintenanceRepository  extends JpaRepository<MaintenanceEntity, Long>, 
												CrudRepository<MaintenanceEntity, Long>, 
												JpaSpecificationExecutor<MaintenanceEntity> {

	@Query("SELECT f FROM MaintenanceEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId")
	List<MaintenanceEntity> getAll(Long apartmentId, Long sessionId);
	
	@Query("SELECT f FROM MaintenanceEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId AND f.flatId = :flatId")
	List<MaintenanceEntity> getAll(Long apartmentId, Long sessionId, Long flatId);
	
	@Query("SELECT f FROM MaintenanceEntity f WHERE f.apartmentId = :apartmentId AND f.flatId = :flatId AND f.isActive = 1 ORDER BY f.createdDate DESC")
	List<MaintenanceEntity> getAllActiveMaintenanceForFlat(Long apartmentId, Long flatId);
	
	@Query("SELECT f FROM MaintenanceEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId")
	List<MaintenanceEntity> getAll(Long apartmentId, Long sessionId, Pageable pageable);
}
