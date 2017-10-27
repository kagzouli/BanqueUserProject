package com.exakaconsulting.exabanque.service;

import java.math.BigDecimal;
import java.util.List;

import com.exakaconsulting.exabanque.exception.ExaMaxAmountCreditException;
import com.exakaconsulting.exabanque.exception.ExaNegativeBalanceAmountException;
import com.exakaconsulting.exabanque.exception.UserExaBanqueNotFoundException;

public interface IExabanqueService {
	
	
	/**
	 * Method to retrieve the balance of the user.<br/>
	 * 
	 * @param userIdentifier The user identifier.<br/>
	 * @return Return the balance for the user.<br/>
	 * @throws UserExaBanqueNotFoundException User not found exception.<br/>
	 */
	BigDecimal retrieveBalanceAmount(final String userIdentifier) throws UserExaBanqueNotFoundException;
	
	
	/**
	 * Method to retrieve all the list operations.<br/>
	 * 
	 * @param identifier
	 * @return
	 * @throws UserExaBanqueNotFoundException
	 */
	List<ExaAccountOperationBean> retrieveListOperations(final AccountOperationStateParam accountOperation) throws UserExaBanqueNotFoundException;

	
	/**
	 * Amount to credit amount for identifier.<br/>
	 * 
	 * @param operationUserParam User Identifier.<br/>
	 * @throws MaxAmountCreditException Max amount exception.<br/>
	 * @throws UserExaBanqueNotFoundException user not found exception.<br/>
	 */
	void creditAmount(final OperationUserParam operationUserParam) throws ExaMaxAmountCreditException , UserExaBanqueNotFoundException;

	
	/**
	 * Method to debit amount for the user identifier.<br/>
	 * 
	 * @param operationUserParam
	 * @return BigDecimal Amount after the debit.<br/>
	 * @throws RoleAlreadyExistsException
	 * @throws UserExaBanqueNotFoundException user not found exception.<br/>
	 */
	BigDecimal debitAmount(final OperationUserParam operationUserParam) throws ExaNegativeBalanceAmountException, UserExaBanqueNotFoundException;


}
