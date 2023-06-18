package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.drps.ams.entity.EmailServiceEntity;

public interface EmailServiceRepository extends JpaRepository<EmailServiceEntity, Long> {

	@Query("SELECT e FROM EmailServiceEntity e WHERE e.apartmentId = :apartmentId")
	List<EmailServiceEntity> getByApartmentId(Long apartmentId);
	
	/**
	The Desciption of the method to explain what the method does
	@param the parameters used by the method 
	@return the value returned by the method EmailServiceEntity
	@throws DB Exception
	@implNote data will retrive where type = 'Notification' and name = 'Dues'
	*/
	@Query("SELECT e FROM EmailServiceEntity e WHERE e.apartmentId = :apartmentId AND e.type = 'Notification' AND e.name = 'Dues'")
	EmailServiceEntity getNotificationDues(Long apartmentId);

}