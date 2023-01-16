package com.drps.ams.service;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.NotesDTO;

import lombok.NonNull;

public interface NotesService {

	ApiResponseEntity saveOrUpdate(@NonNull NotesDTO dto) throws Exception;

	ApiResponseEntity getListView(String reqParams) throws Exception;

	ApiResponseEntity get(Long id) throws Exception;

	ApiResponseEntity deleteById(Long id) throws Exception;

	void createSystemNote(String parentObject, Long parentRecordId, String title, String noteText);

}
