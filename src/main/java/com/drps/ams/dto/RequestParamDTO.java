package com.drps.ams.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class RequestParamDTO {
	
	private RequestParamDTO() {	}
	
	public static RequestParamDTO getInstance(String reqParams) {
		try {
			return reqParams!= null ? new ObjectMapper().readValue(reqParams, RequestParamDTO.class) : null;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	String searchFieldName;
	Object searchFieldValue;
	

	String object;
	Long id;
	
	String linkObject;
	Long linkRecordId;
	
	String parentObject;
	String parentFieldName;
	long parentRecordId;
	
	List<ListViewFieldDTO> seacrchFields;
	
	List<QueryOrderByDTO> orderByFields;
}
