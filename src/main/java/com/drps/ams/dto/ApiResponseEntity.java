package com.drps.ams.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class ApiResponseEntity {
	
	int statusCode;
	
	String message;
	
	Object data;
	
	public ApiResponseEntity(int statusCode, String message) {
		super();
		this.statusCode = statusCode;
		this.message = message;
	}

	public ApiResponseEntity(int statusCode, Object data) {
		super();
		this.statusCode = statusCode;
		this.data = data;
	}

	public ApiResponseEntity(int statusCode, String message, Object data) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.data = data;
	}

	

}
