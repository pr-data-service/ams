package com.drps.ams.exception;

public class NoRecordFoundException  extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public NoRecordFoundException(String message) {
		super(message);
	}
	
	public NoRecordFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}