package com.exakaconsulting.banque.service;

import com.exakaconsulting.exception.FunctionalException;

/**
 * Exception amount credit.<br/>
 * 
 * @author Exaka consulting.<br/>
 *
 */
public class UserBanqueNotFoundException extends FunctionalException{

	/**
	 * Serial version UID.<br/>
	 */
	private static final long serialVersionUID = -6143860195765002497L;
	

	public UserBanqueNotFoundException(final String key) {
		super(key);
	}

	
	public UserBanqueNotFoundException(Exception exception) {
		super(exception);
	}
	
	public UserBanqueNotFoundException(final String key , final Exception exception) {
		super(key , exception);
	}
	
}
