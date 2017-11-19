package com.exakaconsulting.banque.dao;

import java.io.Serializable;
import java.math.BigDecimal;

public class OperAccountRepCount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2470635481043562360L;
	
	private String userIdentifier;
	
	private String operationType;
	
	private BigDecimal total;

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}


}
