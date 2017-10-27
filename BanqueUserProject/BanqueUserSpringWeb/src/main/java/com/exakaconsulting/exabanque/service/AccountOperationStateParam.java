package com.exakaconsulting.exabanque.service;

import java.io.Serializable;
import java.util.Date;

public class AccountOperationStateParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 965053776198738304L;
	
	private String userIdentifier;
	
	private Date beginDate;

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
