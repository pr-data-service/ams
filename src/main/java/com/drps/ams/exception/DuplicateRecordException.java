package com.drps.ams.exception;

public class DuplicateRecordException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public DuplicateRecordException(String message) {
		super(message);
	}
	
	public DuplicateRecordException(String message, Throwable cause) {
		super(message, cause);
	}

}