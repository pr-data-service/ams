package com.drps.ams.exception;

public class UserDisabledException  extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public UserDisabledException(String message) {
		super(message);
	}
	
	public UserDisabledException(String message, Throwable cause) {
		super(message, cause);
	}

}