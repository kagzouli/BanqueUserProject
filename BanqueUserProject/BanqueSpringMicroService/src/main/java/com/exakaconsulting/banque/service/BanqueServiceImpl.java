package com.exakaconsulting.banque.service;

import static com.exakaconsulting.IConstantApplication.BANQUE_SERVICE;
import static com.exakaconsulting.IConstantApplication.BIG_DECIMAL_ZERO;
import static com.exakaconsulting.IConstantApplication.CREDIT_OPERATION_TYPE;
import static com.exakaconsulting.IConstantApplication.DEBIT_OPERATION_TYPE;
import static com.exakaconsulting.IConstantApplication.KEY_MAX_AMOUNT_EXCEPTION;
import static com.exakaconsulting.IConstantApplication.MAX_AMOUNT_AUTORIZED;
import static com.exakaconsulting.IConstantApplication.NEGATIVE_BALANCE_AMOUNT_EXCEPTION;
import static com.exakaconsulting.IConstantApplication.TRANSACTIONAL_BANQUE_BEAN;
import static com.exakaconsulting.IConstantApplication.USER_NOT_FOUND_EXCEPTION;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.exakaconsulting.banque.dao.IBanqueDao;
import com.exakaconsulting.banque.dao.IBanqueUserDao;

@Service(BANQUE_SERVICE)
public class BanqueServiceImpl implements IBanqueService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BanqueServiceImpl.class);

	
	@Autowired
	private IBanqueDao banqueDao;
	
	@Autowired
	private IBanqueUserDao userDao;

	@Override
	@Transactional(rollbackFor=Throwable.class, propagation = Propagation.REQUIRED, transactionManager=TRANSACTIONAL_BANQUE_BEAN)
	public void creditAmount(String userIdentifier,final String labelOperation, BigDecimal amount) throws MaxAmountCreditException, UserBanqueNotFoundException {
		
		Assert.hasLength(userIdentifier , "User identifier has to be set");
		Assert.hasLength(labelOperation,"Label operation must be set");
		Assert.notNull(amount , "Amount has to be set");
		Assert.isTrue(amount.compareTo(BIG_DECIMAL_ZERO) > 0, "Amount has to be positive");
		
		LOGGER.info("BEGIN of the method creditAmount of the class " + BanqueServiceImpl.class.getName());
		
		/** 
		 * If the amount max is authorized.<br/>
		 * 
		 */
		if (amount.compareTo(MAX_AMOUNT_AUTORIZED) > 0){
			throw new MaxAmountCreditException(KEY_MAX_AMOUNT_EXCEPTION, MAX_AMOUNT_AUTORIZED);
		}
		
		// Test if the user exists
		this.callRetrieveUser(userIdentifier);
				
		// Credit the amount tot the account
		AccountOperationBean accountCreditBeanBean = new AccountOperationBean(userIdentifier, labelOperation , amount , CREDIT_OPERATION_TYPE , new Date());
		
		banqueDao.insertAccountOperation(accountCreditBeanBean);

		LOGGER.info("END of the method creditAmount of the class " + BanqueServiceImpl.class.getName());

	}

	@Override
	public List<AccountOperationBean> retrieveOperations(String userIdentifier, Date beginDate, Date endDate) throws UserBanqueNotFoundException{
	
		LOGGER.info("BEGIN of the method retrieveOperations of the class " + BanqueServiceImpl.class.getName());
		
		Assert.hasLength(userIdentifier , "User identifier has to be set");
		
		// Test if the user exists
		this.callRetrieveUser(userIdentifier);
	
		List<AccountOperationBean> listAccountOperation = banqueDao.retrieveOperations(userIdentifier, beginDate, endDate);
		
		LOGGER.info("END of the method creditAmount of the class " + BanqueServiceImpl.class.getName());

		return listAccountOperation;
	}

	@Override
	public BigDecimal debitAmount(final String userIdentifier,final String labelOperation, final BigDecimal amount) throws NegativeBalanceAmountException, UserBanqueNotFoundException{
		Assert.hasLength(userIdentifier , "User identifier has to be set");
		Assert.hasLength(labelOperation, "The label of the operation must be set");
		Assert.notNull(amount , "Amount has to be set");
		Assert.isTrue(amount.compareTo(BIG_DECIMAL_ZERO) > 0, "Amount has to be positive");
		
		LOGGER.info("BEGIN of the method debitAmount of the class " + BanqueServiceImpl.class.getName());

		
		// Test if the user exists
		this.callRetrieveUser(userIdentifier);
			
		// The amount retrieve is 0 , we can't debit.
		final BigDecimal amountRetrieve = this.retrieveBalanceAmount(userIdentifier);
		if (amountRetrieve == null || amountRetrieve.compareTo(BIG_DECIMAL_ZERO) < 0){
			throw new NegativeBalanceAmountException(NEGATIVE_BALANCE_AMOUNT_EXCEPTION);
		}
		
		// We get the new future amount after the operation
		final BigDecimal amountCalc = amountRetrieve.subtract(amount);
		if (amountCalc.compareTo(BIG_DECIMAL_ZERO) < 0){
			throw new NegativeBalanceAmountException(NEGATIVE_BALANCE_AMOUNT_EXCEPTION);			
		}
		
		AccountOperationBean accountDebitBean = new AccountOperationBean(userIdentifier, labelOperation , amount , DEBIT_OPERATION_TYPE , new Date());
		banqueDao.insertAccountOperation(accountDebitBean);
		
		LOGGER.info("END of the method debitAmount of the class " + BanqueServiceImpl.class.getName());

		
		return amountCalc;
	}

	@Override
	public BigDecimal retrieveBalanceAmount(String userIdentifier) throws UserBanqueNotFoundException{		
		Assert.hasLength(userIdentifier , "User identifier has to be set");
		
		LOGGER.info("BEGIN of the method retrieveBalanceAmount of the class " + BanqueServiceImpl.class.getName());
		
		// Test if the user exists
		this.callRetrieveUser(userIdentifier);
			
		final BigDecimal sumCreditAccount = banqueDao.retrieveSumAmountOperTypeByIdentifierUser(userIdentifier, CREDIT_OPERATION_TYPE);
		final BigDecimal sumDebitAccount = banqueDao.retrieveSumAmountOperTypeByIdentifierUser(userIdentifier, DEBIT_OPERATION_TYPE);

		LOGGER.info("END of the method retrieveBalanceAmount of the class " + BanqueServiceImpl.class.getName());
		return sumCreditAccount.subtract(sumDebitAccount);
	}
	
	
	
	/**
	 * Method to get the user 
	 * 
	 * @param userIdentifier The user identifier.<br/>
	 * @return
	 * @throws UserBanqueNotFoundException
	 */
	private BanqueUserBean callRetrieveUser(final String userIdentifier) throws UserBanqueNotFoundException{
		BanqueUserBean user = userDao.retrieveUserByIdentifier(userIdentifier);
		
		if (user == null || StringUtils.isBlank(user.getIdentifier())){
			throw new UserBanqueNotFoundException(USER_NOT_FOUND_EXCEPTION);			
		}
		
		return user;
		
	}

}
