package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.LinkFlatDetailsAndUserDetailsEntity;
import com.drps.ams.entity.join.FlatDetailsAndUserDetailsEntity;

@Repository
public interface LinkFlatDetailsAndUserDetailsRepository extends JpaRepository<LinkFlatDetailsAndUserDetailsEntity, Long>{

	@Query( nativeQuery = true, value = "SELECT a.id, a.flat_id, a.user_id, u.first_name, u.last_name, u.contact_No_1 FROM link_flat_details_user_details a, user_details u "
			+ " where a.apartment_id = u.apartment_id AND u.apartment_id = :apartmentId AND a.user_id = u.id AND a.flat_id = :flatId AND a.is_active = 1")
	public List<Object[]> getLinkObjectDetails(Long apartmentId, Long flatId);
	
	@Query( nativeQuery = true, value = "SELECT a.id, a.flat_id, a.user_id, u.first_name, u.last_name, u.contact_No_1 FROM link_flat_details_user_details a, user_details u "
			+ " where a.apartmentId = u.apartmentId AND u.apartmentId = :apartmentId AND a.user_id = u.id AND a.flat_id = :flatId and a.user_id = :userId")
	public List<Object[]> getLinkObjectDetails(Long apartmentId, Integer flatId, Integer userId);
	
	@Query("SELECT l FROM LinkFlatDetailsAndUserDetailsEntity l WHERE l.apartmentId = :apartmentId AND l.flatId = :flatId AND l.userId = :userId AND l.isActive = 1")
	public LinkFlatDetailsAndUserDetailsEntity getActiveLinkObjectDetails(Long apartmentId, Integer flatId, Integer userId);
	
	@Query("SELECT l FROM LinkFlatDetailsAndUserDetailsEntity l WHERE l.apartmentId = :apartmentId AND l.flatId = :flatId AND l.isActive = 1")
	public LinkFlatDetailsAndUserDetailsEntity getActiveLinkObjectDetails(Long apartmentId, Long flatId);
	
	@Query("SELECT l.userId FROM LinkFlatDetailsAndUserDetailsEntity l WHERE l.apartmentId = :apartmentId AND l.flatId = :flatId AND l.isActive = 1")
	public Long getActiveLinkUserId(Long apartmentId, Long flatId);
	
	
	@Query("SELECT l FROM LinkFlatDetailsAndUserDetailsEntity l WHERE l.apartmentId = :apartmentId AND l.flatId = :flatId AND l.isActive = 1")
	public List<LinkFlatDetailsAndUserDetailsEntity> findByFlatId(Long apartmentId, Long flatId);
	
	@Query("SELECT new com.drps.ams.entity.join.FlatDetailsAndUserDetailsEntity(l, f, u) FROM FlatDetailsEntity f"
			+" INNER JOIN LinkFlatDetailsAndUserDetailsEntity l ON l.flatId = f.id AND l.isActive = 1 AND l.apartmentId = f.apartmentId"
			+" INNER JOIN UserDetailsEntity u ON l.userId = u.id AND l.apartmentId = u.apartmentId WHERE f.apartmentId = :apartmentId")
	public List<FlatDetailsAndUserDetailsEntity> getFlatAndUserByApartmentId(Long apartmentId);
}
