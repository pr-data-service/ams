package com.drps.ams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.NotesEntity;

@Repository
public interface NotesRepository extends JpaRepository<NotesEntity, Long>, CrudRepository<NotesEntity, Long>,JpaSpecificationExecutor<NotesEntity> {

	@Query("SELECT f FROM NotesEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId AND f.title = :title")
	List<NotesEntity> findByTitle(Long apartmentId, Long sessionId, String title);
	
	@Query("SELECT f FROM NotesEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId AND f.parentObject = :parentObject AND f.parentRecordId = :parentRecordId AND f.noteType = :noteType")
	List<NotesEntity> findByNoteType(Long apartmentId, Long sessionId, String parentObject, Long parentRecordId, String noteType);
	
	@Query("SELECT f FROM NotesEntity f WHERE f.apartmentId = :apartmentId AND f.sessionId = :sessionId AND f.parentObject = :parentObject AND f.parentRecordId = :parentRecordId")
	List<NotesEntity> findNotes(Long apartmentId, Long sessionId, String parentObject, Long parentRecordId);
	
}
