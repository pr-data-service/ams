package com.drps.ams.exception;

public class ApartmentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -4774280272013467319L;
	
	private static final String defaultMessage = "Apartment not found.";
	
	public ApartmentNotFoundException() {
		super(defaultMessage);
	}
	
	public ApartmentNotFoundException(Throwable cause) {
		super(defaultMessage, cause);
	}

}
