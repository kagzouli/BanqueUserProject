package com.exakaconsulting.banque.web;

import static com.exakaconsulting.IConstantApplication.BALANCE_USER_REST;
import static com.exakaconsulting.IConstantApplication.CREDIT_ACCOUNT_REST;
import static com.exakaconsulting.IConstantApplication.DEBIT_ACCOUNT_REST;
import static com.exakaconsulting.IConstantApplication.KEY_MAX_AMOUNT_EXCEPTION;
import static com.exakaconsulting.IConstantApplication.LIST_OPERATION_REST;
import static com.exakaconsulting.IConstantApplication.MAX_AMOUNT_AUTORIZED;
import static com.exakaconsulting.IConstantApplication.NEGATIVE_BALANCE_AMOUNT_EXCEPTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.exakaconsulting.JsonResult;
import com.exakaconsulting.banque.dao.IBanqueUserDao;
import com.exakaconsulting.banque.service.AccountOperationBean;
import com.exakaconsulting.banque.service.BanqueApplicationTest;
import com.exakaconsulting.banque.service.BanqueUserBean;
import com.exakaconsulting.banque.service.UserBanqueNotFoundException;
import com.exakaconsulting.banque.util.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BanqueApplicationTest.class)
@Transactional
public class BanqueRESTTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(BanqueRESTTest.class);
	
	protected static final String IDENTIFIER_USER_TEST = "ROGFED";

	/** Not found user */
	protected static final String IDENTIFIER_NOT_FOUND_USER = "NOTFOUNDUSER";

	private static final String BANK_USER_MANAGER = "manager";
	private static final String BANK_PASSWORD_MANAGER = "baNkmaNager35#";

	private static final String BANK_USER_COLLABORATOR = "collaborator";
	private static final String BANK_PASSWORD_COLLABORATOR = "collaBoRator35#";

	private static final String GET_REQUEST = "GET";
	private static final String POST_REQUEST = "POST";
	
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private IBanqueUserDao userDao;
	
	@Autowired
	private Filter springSecurityFilterChain;	 

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();

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
	 * 
	 * BEGIN test list operation account.<br/>
	 * 
	 */
	@Test
	public void testListOperationsSuccess() {
		try {
			AccountOperationStateParam accountOperationParam = new AccountOperationStateParam();
			accountOperationParam.setUserIdentifier(IDENTIFIER_USER_TEST);
			accountOperationParam.setBeginDate(DateUtils.fixDate(2016, 12, 17, 0, 0, 0, 0));

			final JsonResult jsonResult = this.retrieveJsonResultConsumes(LIST_OPERATION_REST, accountOperationParam, GET_REQUEST);

			assertNotNull(jsonResult);
			assertEquals(jsonResult.isSuccess(), true);

			List<AccountOperationBean> listAccountOperation = (List<AccountOperationBean>) jsonResult.getResult();
			assertNotNull(listAccountOperation);
			assertEquals(listAccountOperation.size(), 4);
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			assertTrue(false);
		}
	}

	@Test
	public void testListOperationsUserDoesNotExist() {
		// TODO : To decomment
		/*
		 * try { AccountOperationStateParam accountOperationParam = new
		 * AccountOperationStateParam();
		 * accountOperationParam.setUserIdentifier(IDENTIFIER_NOT_FOUND_USER);
		 * accountOperationParam.setBeginDate(DateUtils.fixDate(2016, 12, 17, 0,
		 * 0, 0, 0));
		 * 
		 * this.testFunctionalErrorUserNotFound(LIST_OPERATION_REST,
		 * accountOperationParam, MediaType.APPLICATION_JSON); } catch
		 * (Exception exception) { LOGGER.error(exception.getMessage(),
		 * exception); assertTrue(false); }
		 */
	}

	/**
	 * 
	 * END test list operation account.<br/>
	 * 
	 */

	/**
	 * 
	 * BEGIN test credit account user.<br/>
	 * 
	 */
	@Test
	public void testCreditUserOperationSuccess() {
		try {
			OperationUserParam operationUserParam = new OperationUserParam();
			operationUserParam.setIdentifier(IDENTIFIER_USER_TEST);
			operationUserParam.setAmount(BigDecimal.valueOf(200));
			operationUserParam.setLabelOperation("Credit op test 1");

			JsonResult jsonResult = this.retrieveJsonResultConsumes(CREDIT_ACCOUNT_REST, operationUserParam , POST_REQUEST);

			assertNotNull(jsonResult);
			assertEquals(jsonResult.isSuccess(), true);

		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			assertTrue(false);
		}
	}

	@Test
	public void testCreditAmountExceeded() {
		try {
			OperationUserParam operationUserParam = new OperationUserParam();
			operationUserParam.setIdentifier(IDENTIFIER_USER_TEST);
			operationUserParam.setAmount(MAX_AMOUNT_AUTORIZED.add(BigDecimal.valueOf(20)));
			operationUserParam.setLabelOperation("Credit op test 1");

			this.testFunctionalErrorCode(CREDIT_ACCOUNT_REST, operationUserParam, KEY_MAX_AMOUNT_EXCEPTION, POST_REQUEST);

		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			assertTrue(false);
		}
	}

	@Test
	public void testCreditUserNotFound() {
		/*
		 * try { OperationUserParam operationUserParam = new
		 * OperationUserParam();
		 * operationUserParam.setIdentifier(IDENTIFIER_NOT_FOUND_USER);
		 * operationUserParam.setAmount(BigDecimal.valueOf(200));
		 * operationUserParam.setLabelOperation("Credit op test 1");
		 * 
		 * this.testFunctionalErrorUserNotFound(CREDIT_ACCOUNT_REST,
		 * operationUserParam, MediaType.APPLICATION_JSON);
		 * 
		 * } catch (Exception exception) { LOGGER.error(exception.getMessage(),
		 * exception); assertTrue(false); }
		 */
		// TODO : Test credit user not found.
	}

	/**
	 * 
	 * END test credit account user.<br/>
	 * 
	 */

	/**
	 * 
	 * BEGIN test debit account user.<br/>
	 * 
	 */

	@Test
	public void testDebitUserAccountSuccess() {
		try {
			OperationUserParam operationUserParam = new OperationUserParam();
			operationUserParam.setIdentifier(IDENTIFIER_USER_TEST);
			operationUserParam.setAmount(BigDecimal.valueOf(100));
			operationUserParam.setLabelOperation("Debit op test 1");

			JsonResult jsonResult = this.retrieveJsonResultConsumes(DEBIT_ACCOUNT_REST, operationUserParam , POST_REQUEST);
			assertNotNull(jsonResult);
			assertEquals(jsonResult.isSuccess(), true);
			assertNotNull(jsonResult.getResult());

		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			assertTrue(false);
		}
	}

	@Test
	public void testDebitUserAccountNegativeBalance() {
		try {
			OperationUserParam operationUserParam = new OperationUserParam();
			operationUserParam.setIdentifier(IDENTIFIER_USER_TEST);
			operationUserParam.setAmount(BigDecimal.valueOf(800));
			operationUserParam.setLabelOperation("Debit op test 1");

			this.testFunctionalErrorCode(DEBIT_ACCOUNT_REST, operationUserParam, NEGATIVE_BALANCE_AMOUNT_EXCEPTION, POST_REQUEST);

		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			assertTrue(false);
		}
	}

	@Test
	public void testDebitUserNotFound() {
		/*
		 * try { OperationUserParam operationUserParam = new
		 * OperationUserParam();
		 * operationUserParam.setIdentifier(IDENTIFIER_NOT_FOUND_USER);
		 * operationUserParam.setAmount(BigDecimal.valueOf(70));
		 * operationUserParam.setLabelOperation("Debit op test 1");
		 * 
		 * this.testFunctionalErrorUserNotFound(DEBIT_ACCOUNT_REST,
		 * operationUserParam, MediaType.APPLICATION_JSON);
		 * 
		 * } catch (Exception exception) { LOGGER.error(exception.getMessage(),
		 * exception); assertTrue(false); }
		 */
		// TODO : Test debit user not found.
	}

	/**
	 * 
	 * END test debit account user.<br/>
	 * 
	 */

	/**
	 * 
	 * BEGIN test balance user.<br/>
	 * 
	 */

	@Test
	public void testBalanceUserExists() {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("userIdentifier", IDENTIFIER_USER_TEST);

			final JsonResult jsonResult = this.retrieveJsonResultConsumesForm(BALANCE_USER_REST, params , GET_REQUEST);
			assertNotNull(jsonResult);
			assertEquals(jsonResult.isSuccess(), true);
			assertTrue(jsonResult.getErrors() == null || jsonResult.getErrors().isEmpty());
			assertNotNull(jsonResult.getResult());
			assertTrue(jsonResult.getResult() instanceof Integer);
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			assertTrue(false);
		}

	}

	@Test
	public void testBalanceUserNotFound() {
		/*
		 * 
		 * try { Map<String, String> params = new HashMap<>();
		 * params.put("userIdentifier", IDENTIFIER_NOT_FOUND_USER);
		 * 
		 * final JsonResult jsonResult =
		 * this.retrieveJsonResultConsumesForm(BALANCE_USER_REST, params);
		 * this.testErrorOnJsonResult(jsonResult, USER_NOT_FOUND_EXCEPTION);
		 * 
		 * 
		 * } catch (Exception exception) { LOGGER.error(exception.getMessage(),
		 * exception); assertTrue(false); }
		 */
		// TODO : Code to decomment when user is done.
	}

	/**
	 * 
	 * END test credit account user.<br/>
	 * 
	 */

	/**
	 * Method to retrieve jsonResult from the request and initial request object
	 * 
	 * @param urlToTest
	 * @param initialObject
	 * @param requestType
	 * @return
	 * @throws Exception
	 */
	protected JsonResult retrieveJsonResultConsumes(final String urlToTest, final Object initialObject, final String requestType)
			throws Exception {
		
		MockHttpServletRequestBuilder resultActions = null;

		switch(requestType){
			case GET_REQUEST:
				resultActions = get(urlToTest);
				break;
			default:
				resultActions = post(urlToTest);
				break;
		}

		final String json = mapper.writeValueAsString(initialObject);
		
		final MvcResult mvcResult = mockMvc.perform(resultActions.with(httpBasic(BANK_USER_MANAGER, BANK_PASSWORD_MANAGER)).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

		assertNotNull(mvcResult);

		final MockHttpServletResponse response = mvcResult.getResponse();
		assertNotNull(response);

		final String responseContent = response.getContentAsString();
		assertNotNull(responseContent);

		final JsonResult jsonResult = mapper.readValue(responseContent, JsonResult.class);
		return jsonResult;
	}

	/**
	 * Method to retrieve jsonResult from the request and initial request object
	 * 
	 * @param urlToTest
	 * @param initialObject
	 * @return
	 * @throws Exception
	 */
	protected JsonResult retrieveJsonResultConsumesForm(final String urlToTest, final Map<String, String> params, final String requestType)
			throws Exception {

		MockHttpServletRequestBuilder resultActions = null;
		switch(requestType){
		case GET_REQUEST:
			resultActions = get(urlToTest);
			break;
		default:
			resultActions = post(urlToTest);
			break;
		}
		
		MockHttpServletRequestBuilder requestBuilder = resultActions.accept(MediaType.APPLICATION_JSON);

		if (params != null) {
			for (Map.Entry<String, String> paramEntry : params.entrySet()) {
				requestBuilder = requestBuilder.param(paramEntry.getKey(), paramEntry.getValue());
			}
		}

		final MvcResult mvcResult = mockMvc.perform(requestBuilder.with(httpBasic(BANK_USER_COLLABORATOR,BANK_PASSWORD_COLLABORATOR))).andReturn();

		final MockHttpServletResponse response = mvcResult.getResponse();
		assertNotNull(response);

		final String responseContent = response.getContentAsString();
		assertNotNull(responseContent);

		final JsonResult jsonResult = mapper.readValue(responseContent, JsonResult.class);
		return jsonResult;
	}

	/**
	 * Test the functional error keyCodeError.<br/>
	 * 
	 * @param urlToTest
	 * @param initialObject
	 * @param keyCodeError
	 * @param requestType/
	 * @throws Exception
	 */
	protected void testFunctionalErrorCode(final String urlToTest, final Object initialObject,
			final String keyCodeError, final String requestType) throws Exception {
		final JsonResult jsonResult = this.retrieveJsonResultConsumes(urlToTest, initialObject , requestType);
		this.testErrorOnJsonResult(jsonResult, keyCodeError);
	}

	protected void testErrorOnJsonResult(final JsonResult jsonResult, final String keyCodeError) {
		assertNotNull(jsonResult);
		assertEquals(jsonResult.isSuccess(), false);
		final List<String> listErrors = jsonResult.getErrors();
		assertNotNull(listErrors);

		final String errorRecup = listErrors.stream().filter(error -> keyCodeError.equals(error)).findAny()
				.orElse(null);
		assertNotNull(errorRecup);
		assertEquals(errorRecup, keyCodeError);

	}


}
