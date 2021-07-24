package com.bank.userregistrationservice.Exception;

public class UserRegistrationServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UserRegistrationServiceException(String errorMessage) {
		super(errorMessage);
	}

}
