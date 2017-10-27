package com.exakaconsulting.exabanque.exception;

public class TechnicalException extends RuntimeException{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7934511653161908054L;

	public TechnicalException(final String message){
		super(message);
	}
	
	public TechnicalException(final Exception exception){
		super(exception);
	}
	
	public TechnicalException(final String message, final Exception exception){
		super(message , exception);
	}
	
}
