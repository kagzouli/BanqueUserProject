package com.exakaconsulting.banque.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IBanqueService {
	
	/**
	 * Amount to credit amount for identifier.<br/>
	 * 
	 * @param userIdentifier User Identifier.<br/>
	 * @param labelOperation label of the operation.<br/>
	 * @param amount Amount.<br/>
	 * @throws MaxAmountCreditException Max amount exception.<br/>
	 * @throws UserBanqueNotFoundException user not found exception.<br/>
	 */
	void creditAmount(final String userIdentifier ,final String labelOperation, final BigDecimal amount) throws MaxAmountCreditException , UserBanqueNotFoundException;

	/**
	 * Method to retrieve all the operations for the user.<br/>
	 * 
	 * @param userIdentifier.<br/>
	 * @param beginDate begin date to retrieve operations, if null takes everything until endDate.<br/>
	 * @param endDate end date to retrieve operations, if null takes everything from beginDate.<br/>
	 * @return Return list of account operations.<br/>
	 * @throws UserBanqueNotFoundException user not found exception.<br/>
	 */
	List<AccountOperationBean> retrieveOperations(final String userIdentifier ,final Date beginDate , final Date endDate) throws UserBanqueNotFoundException;
	
	/**
	 * Method to debit amount for the user identifier.<br/>
	 * 
	 * @param userIdentifier
	 * @param labelOperation
	 * @param amount
	 * @return BigDecimal Amount after the debit.<br/>
	 * @throws RoleAlreadyExistsException
	 * @throws UserBanqueNotFoundException user not found exception.<br/>
	 */
	BigDecimal debitAmount(final String userIdentifier,final String labelOperation, final BigDecimal amount) throws NegativeBalanceAmountException, UserBanqueNotFoundException;
	
	/**
	 * Method to retrieve the balance of the user.<br/>
	 * 
	 * @param userIdentifier The user identifier.<br/>
	 * @return Return the balance for the user.<br/>
	 * @throws UserBanqueNotFoundException User not found exception.<br/>
	 */
	BigDecimal retrieveBalanceAmount(final String userIdentifier) throws UserBanqueNotFoundException;
}
