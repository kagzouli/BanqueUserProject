package com.exakaconsulting.banque.service;

import static com.exakaconsulting.IConstantApplication.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.exakaconsulting.banque.dao.IBanqueUserDao;

public class BanqueServiceTest extends AbstractBanqueService{

	@Autowired
	@Qualifier(BANQUE_SERVICE)
	private IBanqueService banqueService;
	
	@MockBean
	private IBanqueUserDao userDao;
	
	
	  

    @Before
      public void setup() {
    	   BanqueUserBean banqueUser = new BanqueUserBean();
    	   banqueUser.setIdentifier(IDENTIFIER_USER_TEST);
    	   banqueUser.setFirstName("mock");
    	   banqueUser.setLastName("mock");
           try {
			Mockito.when(userDao.retrieveUserByIdentifier(IDENTIFIER_USER_TEST)).thenReturn(banqueUser);
		} catch (UserBanqueNotFoundException e) {
			
		}
      }

	
	
	/**
	 * BEGIN test list account operation
	 * 
	 */
	
	@Test
	public void testAllAccountOperationBeanUser(){		
		try {
			List<AccountOperationBean> listAccountOperation = banqueService.retrieveOperations(IDENTIFIER_USER_TEST, null, null);
			assertNotNull(listAccountOperation);
			assertEquals(listAccountOperation.size() , 4);
			assertEquals(listAccountOperation.get(0).getLabel(),"Debit2");
		} catch (UserBanqueNotFoundException e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testLastOperationJustBeforeTestBegin(){
		final Date beginDate = this.fixDate(2016, 12, 30, 12, 11, 13, 0);		
		try {
			List<AccountOperationBean> listAccountOperation = banqueService.retrieveOperations(IDENTIFIER_USER_TEST, beginDate, null);
			assertNotNull(listAccountOperation);
			assertEquals(listAccountOperation.size() , 1);
			assertEquals(listAccountOperation.get(0).getLabel(),"Debit2");
		} catch (UserBanqueNotFoundException e) {
			assertTrue(false);
		}		
	}
	
	@Test
	public void testLastOperationJustAfterTestBegin(){
		final Date beginDate = this.fixDate(2016, 12, 30, 12, 11, 14, 0);		
		try {
			List<AccountOperationBean> listAccountOperation = banqueService.retrieveOperations(IDENTIFIER_USER_TEST, beginDate, null);
			assertTrue(listAccountOperation == null ||listAccountOperation.isEmpty());
		} catch (UserBanqueNotFoundException e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testFirstOperationJustBeforeTestEnd(){
		final Date endDate = this.fixDate(2016, 12, 29, 14, 32, 0, 0);		
		try {
			List<AccountOperationBean> listAccountOperation = banqueService.retrieveOperations(IDENTIFIER_USER_TEST, null, endDate);
			assertTrue(listAccountOperation == null || listAccountOperation.isEmpty());
		} catch (UserBanqueNotFoundException e) {
			assertTrue(false);
		}		
	}


	@Test
	public void testFirstOperationJustAfterTestEnd(){
		final Date endDate = this.fixDate(2016, 12, 29, 14, 33, 0, 0);		
		try {
			List<AccountOperationBean> listAccountOperation = banqueService.retrieveOperations(IDENTIFIER_USER_TEST, null, endDate);
			assertNotNull(listAccountOperation);
			assertEquals(listAccountOperation.size() , 1);
			assertEquals(listAccountOperation.get(0).getLabel(), "Credit1");
		} catch (UserBanqueNotFoundException e) {
			assertTrue(false);
		}		
	}
	
	@Test
	public void testValidOperationsForBeginAndEndDate(){
		final Date beginDate = this.fixDate(2016, 12, 29, 14, 32, 0, 0);		
		final Date endDate = this.fixDate(2016, 12, 30, 12, 14, 0, 0);		

		try {
			List<AccountOperationBean> listAccountOperation = banqueService.retrieveOperations(IDENTIFIER_USER_TEST, beginDate, endDate);
			assertNotNull(listAccountOperation);
			assertEquals(listAccountOperation.size() , 4);
			assertEquals(listAccountOperation.get(0).getLabel(),"Debit2");
		} catch (UserBanqueNotFoundException e) {
			assertTrue(false);
		}		
	}
	
	/**
	 * END test list account operation
	 * 
	 */

	
	/**
	 * BEGIN credit accountOperation
	 * 
	 */

	
	
	
	@Test
	public void testInsertMaxCreditAmountExceeded(){
		
		try {
			banqueService.creditAmount(IDENTIFIER_USER_TEST, "test credit", MAX_AMOUNT_AUTORIZED.add(MAX_AMOUNT_AUTORIZED));
			assertTrue(false);
		} catch (MaxAmountCreditException e) {
			assertTrue(true);
		} catch (UserBanqueNotFoundException e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testCreditAccountSuccess(){

		try {
			final String testLabelOperation = "test credit";
			final BigDecimal creditAmount = BigDecimal.valueOf(100);
			
			// Credit amount operation 
			banqueService.creditAmount(IDENTIFIER_USER_TEST, testLabelOperation, creditAmount);
			
			final Calendar justBeforeDate = Calendar.getInstance();
			justBeforeDate.add(Calendar.MINUTE, -5);
			
			// Retrieve the list of operation account user
			List<AccountOperationBean> listAccountOperation =  banqueService.retrieveOperations(IDENTIFIER_USER_TEST, justBeforeDate.getTime(), null);
			
			assertNotNull(listAccountOperation);
			assertEquals(listAccountOperation.size() , 1);
			
			AccountOperationBean accountOperationFilter = listAccountOperation.stream().filter(accountOperation -> testLabelOperation.equals(accountOperation.getLabel())).findAny().orElse(null);
			assertNotNull(accountOperationFilter);
			assertEquals(accountOperationFilter.getAmount(), creditAmount);
			assertEquals(accountOperationFilter.getUserIdentifier(), IDENTIFIER_USER_TEST);
			assertEquals(accountOperationFilter.getOperationType(), CREDIT_OPERATION_TYPE);
			
		} catch (MaxAmountCreditException | UserBanqueNotFoundException exception) {
			assertTrue(false);
		}
	}
	
	/**
	 * END credit account operation
	 * 
	 */


	/**
	 * BEGIN debit account operation
	 * 
	 */
	
	@Test
	public void testDebitNegativeBalanceAccount(){

		final String testLabelOperation = "test credit";
		final BigDecimal debitAmount = BigDecimal.valueOf(490);
		
		try {
			banqueService.debitAmount(IDENTIFIER_USER_TEST, testLabelOperation, debitAmount);
			assertTrue(false);
		} catch (NegativeBalanceAmountException e) {
			assertTrue(true);
		} catch (UserBanqueNotFoundException e) {
			assertTrue(false);
		}
	}
		
	@Test
	public void testDebitAccountSuccess(){
		final String testLabelOperation = "test credit";
		final BigDecimal debitAmount = BigDecimal.valueOf(70);
		
		try {
			// Debit the account
			banqueService.debitAmount(IDENTIFIER_USER_TEST, testLabelOperation, debitAmount);
			
			final Calendar justBeforeDate = Calendar.getInstance();
			justBeforeDate.add(Calendar.MINUTE, -5);
			
			// Retrieve the list of operation account user
			List<AccountOperationBean> listAccountOperation =  banqueService.retrieveOperations(IDENTIFIER_USER_TEST, justBeforeDate.getTime(), null);
			
			assertNotNull(listAccountOperation);
			assertEquals(listAccountOperation.size() , 1);
			
			AccountOperationBean accountOperationFilter = listAccountOperation.stream().filter(accountOperation -> testLabelOperation.equals(accountOperation.getLabel())).findAny().orElse(null);
			assertNotNull(accountOperationFilter);
			assertEquals(accountOperationFilter.getAmount(), debitAmount);
			assertEquals(accountOperationFilter.getUserIdentifier(), IDENTIFIER_USER_TEST);
			assertEquals(accountOperationFilter.getOperationType(), DEBIT_OPERATION_TYPE);

		} catch (NegativeBalanceAmountException | UserBanqueNotFoundException exception) {
			assertTrue(false);
		}
	}
	
	/**
	 * END debit account operation
	 * 
	 */


	/**
	 * BEGIN balance user
	 * 
	 */
		
	@Test
	public void testBalanceSuccess(){
		try {
			BigDecimal balance = banqueService.retrieveBalanceAmount(IDENTIFIER_USER_TEST);
			assertEquals(balance , BigDecimal.valueOf(300));
		} catch (UserBanqueNotFoundException e) {
			assertTrue(false);
		}
	}

	

}
