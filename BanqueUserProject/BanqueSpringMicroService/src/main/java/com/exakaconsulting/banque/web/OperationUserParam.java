package com.exakaconsulting.banque.web;

import java.io.Serializable;
import java.math.BigDecimal;

public class OperationUserParam implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3304457046339297388L;

	private String identifier;
	
	private String labelOperation;
	
	private BigDecimal amount;
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getLabelOperation() {
		return labelOperation;
	}

	public void setLabelOperation(String labelOperation) {
		this.labelOperation = labelOperation;
	}


	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
