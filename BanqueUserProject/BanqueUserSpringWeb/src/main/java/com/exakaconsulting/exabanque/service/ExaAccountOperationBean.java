package com.exakaconsulting.exabanque.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class ExaAccountOperationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2068078747564402727L;
	
	@ApiModelProperty(value="The identifier of the user.", required=true)
	private String userIdentifier;

	@ApiModelProperty(value="The label of the operation", required=true)
	private String label;
	
	@ApiModelProperty(value="The amount of the operation", required=true)
	private BigDecimal amount;
	
	@ApiModelProperty(value="The operation type", required=true)
	private String operationType;
	
	@ApiModelProperty(value="The operation date", required=true)
	private Date operationDate;
	
	public ExaAccountOperationBean(){
		super();
	}
	
	public ExaAccountOperationBean(String userIdentifier, String label, BigDecimal amount, String operationType,
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
