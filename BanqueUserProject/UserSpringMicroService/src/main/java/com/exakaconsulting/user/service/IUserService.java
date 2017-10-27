package com.exakaconsulting.user.service;

import java.util.List;

public interface IUserService {
	
	
	/**
	 * Retrieve the list of the roles.<br/>
	 * 
	 * @return
	 */
	List<RoleBean> retrieveRolesList();


	
	/**
	 * Retrieve the list of users.<br/>
	 * 
	 * @return Return the list of users.<br/>
	 */
	List<UserLightBean> retrieveUsersList();
	
	
	/**
	 * Update the user.<br/>
	 * 
	 * @param userBean The user.<br/>
	 * @throws UserNotFoundException
	 */
	void updateUser(UserBean userBean) throws UserNotFoundException;
	
	/**
	 * Insert the user.<br/>
	 * 
	 * @param userBean The user.<br/>
	 * @return Return the id of the new user.<br/>
	 * @exception if the user already exists.<br/>
	 */
	Integer insertUser(UserBean userBean) throws UserAlreadyExistsException;
	
	/**
	 * Delete the users.<br/>
	 * 
	 * @param userCode the user code.<br/>
	 * @throws UserNotFoundException The user not found.<br/>
	 */
	void deleteUser(final String userCode) throws UserNotFoundException; 
	

	/**
	 * Retrieve the user by code.<br/>
	 * 
	 * @param userCode
	 * @return
	 * @throws UserNotFoundException user not found exception.<br/>
	 */
	UserBean retrieveUserByCode(final String userCode) throws UserNotFoundException;
}
