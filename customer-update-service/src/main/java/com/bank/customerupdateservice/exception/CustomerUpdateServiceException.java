package com.bank.customerupdateservice.exception;

public class CustomerUpdateServiceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CustomerUpdateServiceException(String errorMessage) {
		super(errorMessage);
	}

}
