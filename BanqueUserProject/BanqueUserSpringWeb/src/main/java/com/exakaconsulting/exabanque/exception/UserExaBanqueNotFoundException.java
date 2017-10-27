package com.exakaconsulting.exabanque.exception;

public class UserExaBanqueNotFoundException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6372187924583216364L;

	public UserExaBanqueNotFoundException(final String message){
		super(message);
	}
	
	public UserExaBanqueNotFoundException(final Exception exception){
		super(exception);
	}
	
	public UserExaBanqueNotFoundException(final String message, final Exception exception){
		super(message , exception);
	}
	

}
