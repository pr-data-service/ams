package com.drps.ams.exception;

public class UserContextNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public UserContextNotFoundException(String message) {
		super(message);
	}
	
	public UserContextNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}