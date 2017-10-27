package com.exakaconsulting.exception;

public class TechnicalException extends RuntimeException{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3738687634423711007L;

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
