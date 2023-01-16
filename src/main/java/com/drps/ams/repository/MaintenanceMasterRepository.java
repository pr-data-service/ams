package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.drps.ams.entity.MaintenanceMasterEntity;

@Repository
public interface MaintenanceMasterRepository  extends JpaRepository<MaintenanceMasterEntity, Long>, CrudRepository<MaintenanceMasterEntity, Long>,JpaSpecificationExecutor<MaintenanceMasterEntity> {

	List<MaintenanceMasterEntity> findByAmount(Double amount);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("UPDATE MaintenanceMasterEntity m SET m.isActive=0 WHERE m.id !=:id")
	void inActiveAll(Long id);
	
	@Query("SELECT m FROM MaintenanceMasterEntity m WHERE m.isActive = 1")
	MaintenanceMasterEntity getActiveMaintenance();
}
