package com.exakaconsulting.exabanque.service;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class OperationUserParam implements Serializable{
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 8019736073541044305L;

	@ApiModelProperty(value="The user identifier to make the operation", required=true)
	private String identifierUser;

	@ApiModelProperty(value="The label operation", required=true)
	private String labelOperation;

	@ApiModelProperty(value="The amount of the operation", required=true)
	private BigDecimal amount;
	
	public String getIdentifierUser() {
		return identifierUser;
	}

	public void setIdentifierUser(String identifierUser) {
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
