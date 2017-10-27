package com.exakaconsulting.user.web;

import static com.exakaconsulting.IConstantUserApplication.USERS_BYCODE_REST;
import static com.exakaconsulting.IConstantUserApplication.USERS_LIST_REST;
import static com.exakaconsulting.IConstantUserApplication.ROLES_LIST_REST;
import static com.exakaconsulting.IConstantUserApplication.INSERT_USER_REST;
import static com.exakaconsulting.IConstantUserApplication.UPDATE_USER_REST;
import static com.exakaconsulting.IConstantUserApplication.DELETE_USER_REST;
import static com.exakaconsulting.IConstantUserApplication.USER_NOT_FOUND_EXCEPTION;
import static com.exakaconsulting.IConstantUserApplication.USER_ALREADY_EXISTS_EXCEPTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import com.exakaconsulting.user.service.RoleBean;
import com.exakaconsulting.user.service.UserApplicationTest;
import com.exakaconsulting.user.service.UserBean;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplicationTest.class)
@Transactional
public class UserRESTTest {

	protected static final String IDENTIFIER_USER_TEST = "ROGFED";

	protected static final String IDENTIFIER_USER_NOTEXISTS = "USERNOTEXISTS";

	protected static final String IDENTIFIER_NEW_USER = "NOVDJO";

	protected static final String FIRST_NAME_NEW_USER = "Novak";

	protected static final String LAST_NAME_NEW_USER = "Djokovic";

	protected static final String LOCALE_NEW_USER = "EN";

	protected static final String FIRST_NAME_USER_TEST = "Roger";

	protected static final String LAST_NAME_USER_TEST = "Federer";

	protected static final String ROLE_TEST_USER = "role1";
	
	private static final String USERNAME = "administrator";

	private static final String PASSWORD = "adMinisTrator35#";
	
	private static final String GET_REQUEST = "GET";
	private static final String POST_REQUEST = "POST";


	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private Filter springSecurityFilterChain;	 


	private static final Logger LOGGER = LoggerFactory.getLogger(UserRESTTest.class);

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
	}

	/**
	 * 
	 * BEGIN test list all users.<br/>
	 * 
	 */
	@Test
	public void listAllUsers() {
		try {

			final JsonResult jsonResult = this.retrieveJsonResultConsumesForm(USERS_LIST_REST, new HashMap<>(), GET_REQUEST);

			assertNotNull(jsonResult);
			assertEquals(jsonResult.isSuccess(), true);

			List<Map<String, Object>> usersList = (List<Map<String, Object>>) jsonResult.getResult();
			assertNotNull(usersList);
			assertEquals(usersList.size(), 3);
			final Map<String, Object> mapUserTest = usersList.get(2);
			assertEquals(mapUserTest.get("identifierCodeUser"), IDENTIFIER_USER_TEST);
			assertEquals(mapUserTest.get("firstName"), FIRST_NAME_USER_TEST);
			assertEquals(mapUserTest.get("lastName"), LAST_NAME_USER_TEST);
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			assertTrue(false);
		}
	}

	/**
	 * 
	 * END test list all users.<br/>
	 * 
	 */

	/**
	 * 
	 * BEGIN test retrieve user by code.<br/>
	 * 
	 */

	@Test
	public void retrieveUserExistByCode() {
		try {

			Map<String, String> params = new HashMap<>();
			params.put("userCode", IDENTIFIER_USER_TEST);

			final JsonResult jsonResult = this.retrieveJsonResultConsumesForm(USERS_BYCODE_REST, params, GET_REQUEST);

			assertNotNull(jsonResult);
			assertEquals(jsonResult.isSuccess(), true);

			final Map<String, Object> mapUserBean = (Map<String, Object>) jsonResult.getResult();
			this.testUserExists(mapUserBean);

		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			assertTrue(false);
		}
	}

	@Test
	public void retrieveUserNotExistByCode() {
		try {

			Map<String, String> params = new HashMap<>();
			params.put("userCode", IDENTIFIER_USER_NOTEXISTS);

			final JsonResult jsonResult = this.retrieveJsonResultConsumesForm(USERS_BYCODE_REST, params, GET_REQUEST);

			assertNotNull(jsonResult);
			this.testErrorOnJsonResult(jsonResult, USER_NOT_FOUND_EXCEPTION);

		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			assertTrue(false);
		}
	}

	/**
	 * 
	 * END test retrieve user by code.<br/>
	 * 
	 */

	/**
	 * 
	 * BEGIN test list all roles.<br/>
	 * 
	 */
	@Test
	public void testListAllRoles() {
		try {

			final JsonResult jsonResult = this.retrieveJsonResultConsumesForm(ROLES_LIST_REST, new HashMap<>(), GET_REQUEST);

			assertNotNull(jsonResult);
			assertEquals(jsonResult.isSuccess(), true);

			List<Map<String, Object>> rolesList = (List<Map<String, Object>>) jsonResult.getResult();
			assertNotNull(rolesList);
			assertEquals(rolesList.size(), 4);
			final Map<String, Object> mapRoleTest = rolesList.get(0);
			assertEquals(mapRoleTest.get("roleCode"), ROLE_TEST_USER);
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			assertTrue(false);
		}
	}

	/**
	 * 
	 * END test list all roles.<br/>
	 * 
	 */

	/**
	 * 
	 * BEGIN test insert user.<br/>
	 * 
	 */

	@Test
	public void testInsertUserSuccess() {
		UserBean userBean = new UserBean();
		userBean.setIdentifierCodeUser(IDENTIFIER_NEW_USER);
		userBean.setFirstName(FIRST_NAME_NEW_USER);
		userBean.setLastName(LAST_NAME_NEW_USER);
		userBean.setLocale(new Locale(LOCALE_NEW_USER));

		List<RoleBean> listRoles = new ArrayList<>();

		RoleBean role1 = new RoleBean();
		role1.setRoleCode(ROLE_TEST_USER);
		listRoles.add(role1);

		RoleBean role2 = new RoleBean();
		role2.setRoleCode("role4");
		listRoles.add(role2);

		userBean.setListRoles(listRoles);

		try {
			JsonResult jsonResult = this.retrieveJsonResultConsumes(INSERT_USER_REST, userBean, POST_REQUEST);
			assertNotNull(jsonResult);
			assertEquals(jsonResult.isSuccess(), true);
			assertTrue(jsonResult.getErrors() == null || jsonResult.getErrors().isEmpty());
			assertTrue(jsonResult.getResult() == null);

		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void testInsertUserAlreadyExists() {
		UserBean userBean = new UserBean();
		userBean.setIdentifierCodeUser(IDENTIFIER_USER_TEST);
		userBean.setFirstName(FIRST_NAME_NEW_USER);
		userBean.setLastName(LAST_NAME_NEW_USER);
		userBean.setLocale(new Locale(LOCALE_NEW_USER));

		List<RoleBean> listRoles = new ArrayList<>();

		RoleBean role1 = new RoleBean();
		role1.setRoleCode(ROLE_TEST_USER);
		listRoles.add(role1);

		RoleBean role2 = new RoleBean();
		role2.setRoleCode("role4");
		listRoles.add(role2);

		userBean.setListRoles(listRoles);

		try {
			this.testFunctionalErrorCode(INSERT_USER_REST, userBean, USER_ALREADY_EXISTS_EXCEPTION, POST_REQUEST);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * 
	 * END test insert user.<br/>
	 * 
	 */
	
	/**
	 * 
	 * BEGIN test update user.<br/>
	 * 
	 */
	
	/**
	 * BEGIN test update user.<br/>
	 * 
	 */
	@Test
	public void testUpdateSuccess(){
		try {

			UserBean userBean = new UserBean();
			final String newFirstName = FIRST_NAME_USER_TEST + "2";
			final String newLastName = LAST_NAME_USER_TEST + "2";
			final String newRole = "role2";
			userBean.setIdentifierCodeUser(IDENTIFIER_USER_TEST);
			userBean.setFirstName(newFirstName);
			userBean.setLastName(newLastName);
			userBean.setLocale(new Locale(LOCALE_NEW_USER));

			List<RoleBean> listRoles = new ArrayList<>();

			RoleBean role1 = new RoleBean();
			role1.setRoleCode(ROLE_TEST_USER);
			listRoles.add(role1);

			RoleBean role2 = new RoleBean();
			role2.setRoleCode(newRole);
			listRoles.add(role2);

			userBean.setListRoles(listRoles);

			// Update the user
			JsonResult jsonResult = this.retrieveJsonResultConsumes(UPDATE_USER_REST, userBean , POST_REQUEST);
			assertNotNull(jsonResult);
			assertEquals(jsonResult.isSuccess(), true);
			assertTrue(jsonResult.getErrors() == null || jsonResult.getErrors().isEmpty());
			assertTrue(jsonResult.getResult() == null);
			
		} catch (Exception exception) {
			assertTrue(false);
		}	
	}
	
	@Test
	public void testUpdateUserNotFound(){
		try {

			UserBean userBean = new UserBean();
			final String newFirstName = FIRST_NAME_USER_TEST + "2";
			final String newLastName = LAST_NAME_USER_TEST + "2";
			final String newRole = "role2";
			userBean.setIdentifierCodeUser(IDENTIFIER_USER_NOTEXISTS);
			userBean.setFirstName(newFirstName);
			userBean.setLastName(newLastName);
			userBean.setLocale(new Locale(LOCALE_NEW_USER));

			List<RoleBean> listRoles = new ArrayList<>();

			RoleBean role1 = new RoleBean();
			role1.setRoleCode(ROLE_TEST_USER);
			listRoles.add(role1);

			RoleBean role2 = new RoleBean();
			role2.setRoleCode(newRole);
			listRoles.add(role2);

			userBean.setListRoles(listRoles);

			// Update the user
			this.testFunctionalErrorCode(UPDATE_USER_REST, userBean, USER_NOT_FOUND_EXCEPTION, POST_REQUEST);
			assertTrue(true);
			
		} catch (Exception exception) {
			assertTrue(false);
		}
		
	}

	
	/**
	 * END test update user.<br/>
	 * 
	 */

	
	/**
	 * BEGIN test delete user.<br/>
	 * 
	 */
	
	@Test
	public void testDeleteUserExists(){
		Map<String, String> params = new HashMap<>();
		params.put("userCode", IDENTIFIER_USER_TEST);

		try{
			final JsonResult jsonResult = this.retrieveJsonResultConsumesForm(DELETE_USER_REST, params, POST_REQUEST);
		
			assertNotNull(jsonResult);
			assertEquals(jsonResult.isSuccess(), true);
			assertTrue(jsonResult.getErrors() == null || jsonResult.getErrors().isEmpty());
			
		}catch(Exception exception){
			assertTrue(false);
		}		
	}

	@Test
	public void testDeleteUserNotExists(){
		Map<String, String> params = new HashMap<>();
		params.put("userCode", IDENTIFIER_USER_NOTEXISTS);

		try{
			final JsonResult jsonResult = this.retrieveJsonResultConsumesForm(DELETE_USER_REST, params , POST_REQUEST);
			this.testErrorOnJsonResult(jsonResult, USER_NOT_FOUND_EXCEPTION);
			assertTrue(true);
			
		}catch(Exception exception){
			assertTrue(false);
		}		
	}

	
	/**
	 * END test delete user.<br/>
	 * 
	 */



	/**
	 * Method to test the existing user.<br/>
	 * 
	 * @param userBean
	 */
	protected void testUserExists(final Map<String, Object> mapUserBean) {
		assertNotNull(mapUserBean);
		assertTrue(!mapUserBean.isEmpty());
		assertEquals(mapUserBean.get("identifierCodeUser"), IDENTIFIER_USER_TEST);
		assertEquals(mapUserBean.get("firstName"), FIRST_NAME_USER_TEST);
		assertEquals(mapUserBean.get("lastName"), LAST_NAME_USER_TEST);

		final List<Map<String, Object>> listRolesMap = (List<Map<String, Object>>) mapUserBean.get("listRoles");
		assertNotNull(listRolesMap);
		assertEquals(listRolesMap.size(), 3);

		Map<String, Object> mapRole = listRolesMap.get(0);
		assertNotNull(mapRole);
		assertTrue(mapRole.size() > 0);
		assertEquals(mapRole.get("roleCode"), ROLE_TEST_USER);

	}

	/**
	 * Method to retrieve jsonResult from the request and initial request object
	 * 
	 * @param urlToTest
	 * @param initialObject
	 * @return
	 * @throws Exception
	 */
	protected JsonResult retrieveJsonResultConsumes(final String urlToTest, final Object initialObject , final String typeRequest)
			throws Exception {

		final String json = mapper.writeValueAsString(initialObject);

		MockHttpServletRequestBuilder resultActions = null;
		switch(typeRequest){
			case GET_REQUEST:
				resultActions = get(urlToTest);
				break;
			default:
				resultActions = post(urlToTest);
				break;
		}
		
		final MvcResult mvcResult = mockMvc.perform(resultActions.with(httpBasic(USERNAME,PASSWORD)).accept(MediaType.APPLICATION_JSON)
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
	protected JsonResult retrieveJsonResultConsumesForm(final String urlToTest, final Map<String, String> params, final String typeRequest)
			throws Exception {

		MockHttpServletRequestBuilder resultActions = null;
		switch(typeRequest){
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

		final MvcResult mvcResult = mockMvc.perform(requestBuilder.with(httpBasic(USERNAME, PASSWORD))).andReturn();

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
	 * @param requestType
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
