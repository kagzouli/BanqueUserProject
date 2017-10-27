package com.exakaconsulting.banque.web;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class AccountOperationStateParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 965053776198738304L;
	
	@ApiModelProperty(value="The  user identifier for the search", required=true)	
	private String userIdentifier;

	@ApiModelProperty(value="The  begin date for the search", required=false)	
	private Date beginDate;

	@ApiModelProperty(value="The  begin end for the search", required=false)	
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
