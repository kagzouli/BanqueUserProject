package com.exakaconsulting.exabanque.service;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class AccountOperationStateParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 965053776198738304L;
	
	@ApiModelProperty(value="The user identifier to search", required=true)
	private String userIdentifier;

	@ApiModelProperty(value="The begin date to search the list of operations on an account")
	private Date beginDate;

	@ApiModelProperty(value="The end date to search the list of operations on an account")
	private Date endDate;

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	
}
