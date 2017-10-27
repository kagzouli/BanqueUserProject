package com.exakaconsulting.banque.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.exakaconsulting.banque.service.AccountOperationBean;

public interface IBanqueDao {


	/**
	 * Insert account operation.<br/>
	 * 
	 * @param accountOperationBean Account operation.<br/>
	 */
	void insertAccountOperation(final AccountOperationBean accountOperationBean);

	/**
	 * Method to retrieve all the operations for the user.<br/>
	 * 
	 * @param userIdentifier.<br/>
	 * @param beginDate begin date to retrieve operations, if null takes everything until endDate.<br/>
	 * @param endDate end date to retrieve operations, if null takes everything from beginDate.<br/>
	 * @return 
	 */
	List<AccountOperationBean> retrieveOperations(final String userIdentifier ,final Date beginDate , final Date endDate);
	
	
	/**
	 * Method to sum amount by operation type and identifierUser.<br/>
	 * 
	 * @param userIdentifier
	 * @param operationType
	 * @return Return the result amount.<br/>
	 */
	BigDecimal retrieveSumAmountOperTypeByIdentifierUser(final String userIdentifier , final String operationType);
	
}
