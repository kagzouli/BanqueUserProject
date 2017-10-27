package com.exakaconsulting.user.service;

import static com.exakaconsulting.IConstantUserApplication.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class UserServiceTest extends AbstractUserService {

	@Autowired
	@Qualifier(USER_SERVICE)
	private IUserService userService;

	/**
	 * BEGIN test users list
	 * 
	 */

	@Test
	public void testAllAccountOperationBeanUser() {
		try {
			List<UserLightBean> usersList = userService.retrieveUsersList();
			assertNotNull(usersList);
			assertEquals(usersList.size(), 3);
			final UserLightBean userTest = usersList.get(2);
			assertEquals(userTest.getIdentifierCodeUser(), IDENTIFIER_USER_TEST);
			assertEquals(userTest.getFirstName(), FIRST_NAME_USER_TEST);
			assertEquals(userTest.getLastName(), LAST_NAME_USER_TEST);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * END test users list
	 * 
	 */

	/**
	 * BEGIN test user by code
	 * 
	 */
	@Test
	public void testUserExists() {
		try {
			final UserBean userBean = userService.retrieveUserByCode(IDENTIFIER_USER_TEST);
			this.testUserBeanExists(userBean);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void testUserNotExists() {
		try {
			userService.retrieveUserByCode(IDENTIFIER_USER_NOTEXISTS);
			assertTrue(false);
		} catch (UserNotFoundException exception) {
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * END test user by code
	 * 
	 */

	/**
	 * BEGIN test roles list
	 * 
	 */
	@Test
	public void testAllRoles() {
		try {
			List<RoleBean> listRoles = userService.retrieveRolesList();

			assertNotNull(listRoles);

			assertEquals(listRoles.size(), 4);

			assertEquals(listRoles.get(0).getRoleCode(), ROLE_TEST_USER);

		} catch (Exception exception) {
			assertTrue(false);
		}
	}

	/**
	 * END test roles list
	 * 
	 */

	/**
	 * BEGIN test insert user.<br/>
	 * 
	 */

	@Test
	public void testInsertNotExistsUser() {
		try {

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

			Integer userIdCreated = userService.insertUser(userBean);

			assertNotNull(userIdCreated);

			final UserBean userDbBean = userService.retrieveUserByCode(IDENTIFIER_NEW_USER);

			assertNotNull(userDbBean);

			assertEquals(userBean.getUserId(), userIdCreated);
			assertEquals(userBean.getIdentifierCodeUser(), IDENTIFIER_NEW_USER);
			assertEquals(userBean.getFirstName(), FIRST_NAME_NEW_USER);
			assertEquals(userBean.getLastName(), LAST_NAME_NEW_USER);
			assertNotNull(userDbBean.getLocale());
			assertEquals(StringUtils.upperCase(userDbBean.getLocale().getLanguage()), LOCALE_NEW_USER);

			final List<RoleBean> listRoleDbBean = userDbBean.getListRoles();
			assertNotNull(listRoleDbBean);

			assertEquals(listRoleDbBean.size(), 2);

			RoleBean roleDbTest = listRoleDbBean.stream().filter(role -> ROLE_TEST_USER.equals(role.getRoleCode()))
					.findAny().orElse(null);
			assertNotNull(roleDbTest);
			assertEquals(roleDbTest.getRoleCode(), ROLE_TEST_USER);
			assertNotNull(roleDbTest.getRoleId());
			assertTrue(roleDbTest.getRoleId() > 0);

		} catch (Exception exception) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testInsertUserExists(){
		try {

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

			userService.insertUser(userBean);

			assertTrue(false);
			
			
		}catch(UserAlreadyExistsException exception){
			assertTrue(true);
		}catch (Exception exception) {
			assertTrue(false);
		}
	}
	
	/**
	 * BEGIN test update user.<br/>
	 * 
	 */
	@Test
	public void testUpdateUserExists(){
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
			userService.updateUser(userBean);

			final UserBean userDbBean = userService.retrieveUserByCode(IDENTIFIER_USER_TEST);

			assertNotNull(userDbBean);

			assertEquals(userDbBean.getIdentifierCodeUser(), IDENTIFIER_USER_TEST);
			assertEquals(userDbBean.getFirstName(), newFirstName);
			assertEquals(userDbBean.getLastName(), newLastName);
			assertNotNull(userDbBean.getLocale());
			assertEquals(StringUtils.upperCase(userDbBean.getLocale().getLanguage()), LOCALE_NEW_USER);

			final List<RoleBean> listRoleDbBean = userDbBean.getListRoles();
			assertNotNull(listRoleDbBean);

			assertEquals(listRoleDbBean.size(), 2);

			RoleBean roleDbTest = listRoleDbBean.stream().filter(role -> ROLE_TEST_USER.equals(role.getRoleCode()))
					.findAny().orElse(null);
			assertNotNull(roleDbTest);
			assertEquals(roleDbTest.getRoleCode(), ROLE_TEST_USER);
			assertNotNull(roleDbTest.getRoleId());
			assertTrue(roleDbTest.getRoleId() > 0);
			
			
			RoleBean newRoleBean = listRoleDbBean.stream().filter(role -> newRole.equals(role.getRoleCode())).findAny().orElse(null);
			assertNotNull(newRoleBean);
			assertNotNull(newRoleBean.getRoleId());
			assertTrue(newRoleBean.getRoleId() > 0);
			
		} catch (Exception exception) {
			assertTrue(false);
		}
		
	}

	@Test
	public void testUpdateUserNotExists(){
	
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
			userService.updateUser(userBean);	
		
			assertTrue(false);
		}catch(UserNotFoundException exception){
			assertTrue(true);
		}catch (Exception exception) {
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
		try {
			// Delete an existing user
			userService.deleteUser(IDENTIFIER_USER_TEST);
		}catch(Exception exception){
			assertTrue(false);
		}
		
		// Test if the deleted user exists.
		try{
			final UserBean userDbBean = userService.retrieveUserByCode(IDENTIFIER_USER_TEST);
			assertNull(userDbBean);
		}catch(UserNotFoundException exception){
			assertTrue(true);
		}catch (Exception exception) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testDeleteUserNotExists(){
		try {
			// Delete an non existing user
			userService.deleteUser(IDENTIFIER_USER_NOTEXISTS);
		}catch(UserNotFoundException exception){
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
	private void testUserBeanExists(final UserBean userBean) {
		assertNotNull(userBean);
		assertEquals(userBean.getIdentifierCodeUser(), IDENTIFIER_USER_TEST);
		assertEquals(userBean.getFirstName(), FIRST_NAME_USER_TEST);
		assertEquals(userBean.getLastName(), LAST_NAME_USER_TEST);

		final List<RoleBean> listRoles = userBean.getListRoles();
		assertNotNull(listRoles);
		assertEquals(listRoles.size(), 3);

		final RoleBean roleBeanTest = listRoles.get(0);
		assertEquals(roleBeanTest.getRoleCode(), ROLE_TEST_USER);

	}

}
