package com.drps.ams.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.ApartmentDetailsEntity;

@Repository
public interface ApartmentDetailsRepository extends JpaRepository<ApartmentDetailsEntity, Long>, 
													CrudRepository<ApartmentDetailsEntity, Long>, 
													JpaSpecificationExecutor<ApartmentDetailsEntity> {

}
