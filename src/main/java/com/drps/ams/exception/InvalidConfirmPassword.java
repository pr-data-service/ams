package com.drps.ams.exception;

public class InvalidConfirmPassword extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public InvalidConfirmPassword(String message) {
		super(message);
	}
	
	public InvalidConfirmPassword(String message, Throwable cause) {
		super(message, cause);
	}
}
