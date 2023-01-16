package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.SessionDetailsEntity;

@Repository
public interface EventsRepository extends JpaRepository<EventsEntity, Long>, CrudRepository<EventsEntity, Long>, JpaSpecificationExecutor<EventsEntity> {

	@Query("SELECT e FROM EventsEntity e WHERE e.apartmentId = :apartmentId AND e.name != 'MAINTENANCE'")
	List<EventsEntity> getAll(Long apartmentId);
	
	@Query("SELECT e FROM EventsEntity e WHERE e.apartmentId = :apartmentId AND e.isActive = 1 AND e.name != 'MAINTENANCE'")
	List<EventsEntity> getAllActiveEntity(Long apartmentId);
	
	@Query("SELECT e FROM EventsEntity e WHERE e.apartmentId = :apartmentId AND e.name = :name AND e.name != 'MAINTENANCE'")
	List<EventsEntity> findByName(Long apartmentId, String name);
	
	@Query("SELECT e FROM EventsEntity e WHERE e.apartmentId = :apartmentId AND e.id IN (:idList) AND e.name != 'MAINTENANCE'")
	List<EventsEntity> getListByIds(Long apartmentId, List<Long> idList);
}
