package com.bank.loan.exception;

public class LoanServiceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LoanServiceException(String errorMessage) {
		super(errorMessage);
	}

}
