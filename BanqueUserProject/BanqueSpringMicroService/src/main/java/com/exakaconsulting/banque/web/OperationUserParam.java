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
	private String identifierUser;
	
	@ApiModelProperty(value="The label of the operation", required=true)
	private String labelOperation;
	
	@ApiModelProperty(value="The amount of the operation", required=true)
	private BigDecimal amount;
	
	public String getIdentifierUser() {
		return identifierUser;
	}

	public void setIdentifier(String identifierUser) {
		this.identifierUser = identifierUser;
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
