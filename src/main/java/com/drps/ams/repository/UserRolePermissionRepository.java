package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.entity.UserRolePermissionEntity;

@Repository
public interface UserRolePermissionRepository extends JpaRepository<UserRolePermissionEntity, Long>, 
	CrudRepository<UserRolePermissionEntity, Long>,
	JpaSpecificationExecutor<UserRolePermissionEntity> {

	@Query("SELECT f FROM UserRolePermissionEntity f WHERE f.apartmentId = :apartmentId")
	List<UserRolePermissionEntity> getAll(Long apartmentId);
	
	@Query("SELECT f FROM UserRolePermissionEntity f WHERE f.apartmentId = :apartmentId AND f.object = :object"
			+ " AND f.role = :role")
	UserRolePermissionEntity get(Long apartmentId, String object, String role);
	
	@Query("SELECT f FROM UserRolePermissionEntity f WHERE f.apartmentId = :apartmentId AND f.role = :role")
	List<UserRolePermissionEntity> findByRole(Long apartmentId, String role);

}
