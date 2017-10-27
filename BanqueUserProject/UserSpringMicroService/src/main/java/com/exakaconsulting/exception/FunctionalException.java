package com.exakaconsulting.exception;

public class FunctionalException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3648983010303870805L;

	public FunctionalException(final String message){
		super(message);
	}
	
	public FunctionalException(final Exception exception){
		super(exception);
	}
	
	public FunctionalException(final String message, final Exception exception){
		super(message , exception);
	}
	
}
