package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.drps.ams.entity.UserDetailsEntity;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long>, CrudRepository<UserDetailsEntity, Long>,JpaSpecificationExecutor<UserDetailsEntity> {

	List<UserDetailsEntity> findByContactNo1(String contactNo1);
	
	@Query("SELECT f FROM UserDetailsEntity f WHERE f.type = 'USER' AND f.contactNo1 = :contactNo1")
	List<UserDetailsEntity> findLoginUserByContactNo1(String contactNo1);
	
	@Query("SELECT f FROM UserDetailsEntity f WHERE f.apartmentId = :apartmentId AND"
			+ "(f.role IS NULL OR f.role != 'SADMIN')")
	List<UserDetailsEntity> getAll(Long apartmentId);
	
	@Query("SELECT f FROM UserDetailsEntity f WHERE f.type = 'USER' AND f.apartmentId = :apartmentId")
	List<UserDetailsEntity> getUserList(Long apartmentId);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("UPDATE UserDetailsEntity U SET U.type = 'USER', U.role = :role, U.password = :defaultPassword WHERE U.id = :id")
	void updateUserRole (String role, String defaultPassword, Long id);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("UPDATE UserDetailsEntity U SET U.type = 'OWNER', U.role = NULL WHERE U.id = :id")
	void removeUserRole (Long id);
	
	@Query("SELECT f FROM UserDetailsEntity f WHERE f.type = 'USER' AND f.role = 'SADMIN'")
	UserDetailsEntity findSAdmin();
}
