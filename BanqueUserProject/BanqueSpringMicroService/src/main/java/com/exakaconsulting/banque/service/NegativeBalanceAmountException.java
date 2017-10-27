package com.exakaconsulting.banque.service;

import com.exakaconsulting.exception.FunctionalException;

/**
 * Exception amount credit.<br/>
 * 
 * @author Exaka consulting.<br/>
 *
 */
public class NegativeBalanceAmountException extends FunctionalException{

	public NegativeBalanceAmountException(final String message) {
		super(message);
	}

	
	public NegativeBalanceAmountException(Exception exception) {
		super(exception);
	}
	
	public NegativeBalanceAmountException(final String message , final Exception exception) {
		super(message , exception);
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -6143860195765002497L;
	
	
	
}
