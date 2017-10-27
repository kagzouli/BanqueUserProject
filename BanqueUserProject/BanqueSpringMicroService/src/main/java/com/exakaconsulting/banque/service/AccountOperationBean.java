package com.exakaconsulting.banque.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AccountOperationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2068078747564402727L;
	
	private String userIdentifier;
	
	private String label;
	
	private BigDecimal amount;
	
	private String operationType;
	
	private Date operationDate;
	
	public AccountOperationBean(){
		super();
	}
	
	public AccountOperationBean(String userIdentifier, String label, BigDecimal amount, String operationType,
			Date operationDate) {
		super();
		this.userIdentifier = userIdentifier;
		this.label = label;
		this.amount = amount;
		this.operationType = operationType;
		this.operationDate = operationDate;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
}
