package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.FlatDetailsEntity;

@Repository
public interface FlatDetailsRepository extends JpaRepository<FlatDetailsEntity, Long>, CrudRepository<FlatDetailsEntity, Long>,JpaSpecificationExecutor<FlatDetailsEntity> {

	@Query("SELECT f FROM FlatDetailsEntity f WHERE f.apartmentId = :apartmentId AND f.flatNo = :flatNo")
	List<FlatDetailsEntity> findByFlatNo(Long apartmentId, String flatNo);
	
	@Query("SELECT f FROM FlatDetailsEntity f WHERE f.apartmentId = :apartmentId AND f.id IN (:flatIdList)")
	List<FlatDetailsEntity> getFlatList(Long apartmentId, List<Long> flatIdList);
	
	@Query("SELECT f FROM FlatDetailsEntity f WHERE f.apartmentId = :apartmentId AND f.id NOT IN (:flatIdList)")
	List<FlatDetailsEntity> getFlatListNotIn(Long apartmentId, List<Long> flatIdList);
	
	@Query("SELECT f FROM FlatDetailsEntity f WHERE f.apartmentId = :apartmentId")
	List<FlatDetailsEntity> getAll(Long apartmentId);
	
	@Query("SELECT f.flatType FROM FlatDetailsEntity f WHERE f.apartmentId = :apartmentId AND f.id = :id")
	String getFlatType(Long apartmentId, Long id);
}
