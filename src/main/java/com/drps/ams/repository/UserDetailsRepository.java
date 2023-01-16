package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.drps.ams.entity.UserDetailsEntity;

public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long>, CrudRepository<UserDetailsEntity, Long>,JpaSpecificationExecutor<UserDetailsEntity> {

	List<UserDetailsEntity> findByContactNo1(String contactNo1);
	
	@Query("SELECT f FROM UserDetailsEntity f WHERE f.apartmentId = :apartmentId")
	List<UserDetailsEntity> getAll(Long apartmentId);
	

}
