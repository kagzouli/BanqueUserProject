package com.exakaconsulting.exabanque.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class OperationUserParam implements Serializable{
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 8019736073541044305L;

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
