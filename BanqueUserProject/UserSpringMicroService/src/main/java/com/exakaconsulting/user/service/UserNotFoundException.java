package com.exakaconsulting.user.service;

import com.exakaconsulting.exception.FunctionalException;

/**
 * Exception amount credit.<br/>
 * 
 * @author Exaka consulting.<br/>
 *
 */
public class UserNotFoundException extends FunctionalException{
	
	private String userCode;

	public UserNotFoundException(final String message , final String userCode) {
		super(message);
		this.userCode = userCode;
	}

	
	public UserNotFoundException(Exception exception , final String userCode) {
		super(exception);
		this.userCode = userCode;

	}
	
	public UserNotFoundException(final String message , final Exception exception , final String userCode) {
		super(message , exception);
		this.userCode = userCode;
	}
	


	public String getUserCode() {
		return userCode;
	}


	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = -6143860195765002497L;
	
	
	
}
