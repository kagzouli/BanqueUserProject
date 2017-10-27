package com.exakaconsulting.exabanque.exception;

public class ExaNegativeBalanceAmountException extends Exception{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1274440440227175207L;

	public ExaNegativeBalanceAmountException(final String message){
		super(message);
	}
	
	public ExaNegativeBalanceAmountException(final Exception exception){
		super(exception);
	}
	
	public ExaNegativeBalanceAmountException(final String message, final Exception exception){
		super(message , exception);
	}
	

}
