package com.exakaconsulting.user.service;

import com.exakaconsulting.exception.FunctionalException;

/**
 * Exception amount credit.<br/>
 * 
 * @author Exaka consulting.<br/>
 *
 */
public class UserAlreadyExistsException extends FunctionalException{

	public UserAlreadyExistsException(final String message) {
		super(message);
	}

	
	public UserAlreadyExistsException(Exception exception) {
		super(exception);
	}
	
	public UserAlreadyExistsException(final String message , final Exception exception) {
		super(message , exception);
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -6143860195765002497L;
	
	
	
}
