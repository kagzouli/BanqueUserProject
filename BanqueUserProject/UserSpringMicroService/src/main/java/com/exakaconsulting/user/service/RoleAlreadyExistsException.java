package com.exakaconsulting.user.service;

import com.exakaconsulting.exception.FunctionalException;

/**
 * Exception amount credit.<br/>
 * 
 * @author Exaka consulting.<br/>
 *
 */
public class RoleAlreadyExistsException extends FunctionalException{

	public RoleAlreadyExistsException(final String message) {
		super(message);
	}

	
	public RoleAlreadyExistsException(Exception exception) {
		super(exception);
	}
	
	public RoleAlreadyExistsException(final String message , final Exception exception) {
		super(message , exception);
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -6143860195765002497L;
	
	
	
}
