package com.exakaconsulting.exabanque.exception;

public class ExaMaxAmountCreditException extends Exception{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1274440440227175207L;

	public ExaMaxAmountCreditException(final String message){
		super(message);
	}
	
	public ExaMaxAmountCreditException(final Exception exception){
		super(exception);
	}
	
	public ExaMaxAmountCreditException(final String message, final Exception exception){
		super(message , exception);
	}
	

}
