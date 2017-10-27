package com.exakaconsulting.banque.service;

import java.math.BigDecimal;

import com.exakaconsulting.exception.FunctionalException;

/**
 * Exception amount credit.<br/>
 * 
 * @author Exaka consulting.<br/>
 *
 */
public class MaxAmountCreditException extends FunctionalException{

	/**
	 * Serial version UID.<br/>
	 */
	private static final long serialVersionUID = -6143860195765002497L;
	
	/** Max amount authorized **/
	private BigDecimal maxAmountAuthorized;

	public MaxAmountCreditException(final String key , final BigDecimal maxAmountAuthorized) {
		super(key);
		this.maxAmountAuthorized = maxAmountAuthorized;
	}

	
	public MaxAmountCreditException(Exception exception , final BigDecimal maxAmountAuthorized) {
		super(exception);
		this.maxAmountAuthorized = maxAmountAuthorized;
	}
	
	public MaxAmountCreditException(final String key , final Exception exception , final BigDecimal maxAmountAuthorized) {
		super(key , exception);
		this.maxAmountAuthorized = maxAmountAuthorized;
	}
	
	public BigDecimal getMaxAmountAuthorized() {
		return maxAmountAuthorized;
	}

	public void setMaxAmountAuthorized(BigDecimal maxAmountAuthorized) {
		this.maxAmountAuthorized = maxAmountAuthorized;
	}

}
