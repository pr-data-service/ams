package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.EmailSetupDetailsEntity;
import com.drps.ams.entity.EventsEntity;

@Repository
public interface EmailSetupDetailsRepository extends JpaRepository<EmailSetupDetailsEntity, Long>, CrudRepository<EmailSetupDetailsEntity, Long>, JpaSpecificationExecutor<EmailSetupDetailsEntity> {
	
	@Query("SELECT e FROM EmailSetupDetailsEntity e WHERE e.apartmentId = :apartmentId AND e.email = :email")
	List<EmailSetupDetailsEntity> findByEmail(Long apartmentId, String email);
	
	@Query("SELECT e FROM EmailSetupDetailsEntity e WHERE e.apartmentId = :apartmentId")
	EmailSetupDetailsEntity findByApartmentId(Long apartmentId);
	
//	@Query(value ="SELECT * FROM email_setup_details where APARTMENT_ID = :apartmentId", nativeQuery= true)
//	EmailSetupDetailsEntity findByApartmentId(Long apartmentId);
}
