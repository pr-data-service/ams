package com.drps.ams.exception;

public class SessionNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3942922507557772769L;
	
	private static final String defaultMessage = "Session not found.";
	
	public SessionNotFoundException() {
		super(defaultMessage);
	}
	
	public SessionNotFoundException(Throwable cause) {
		super(defaultMessage, cause);
	}

}
