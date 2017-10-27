package com.exakaconsulting.banque.web;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class OperationUserParam implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3304457046339297388L;

	@ApiModelProperty(value="The user identifier that makes the operation.", required=true)
	private String identifier;
	
	@ApiModelProperty(value="The label of the operation", required=true)
	private String labelOperation;
	
	@ApiModelProperty(value="The amount of the operation", required=true)
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
