package com.exakaconsulting.user.dao;

import java.util.List;

import com.exakaconsulting.user.service.UserBean;
import com.exakaconsulting.user.service.UserLightBean;

public interface IUserDao {
	
	/**
	 * Retrieve all the users.<br/>
	 * 
	 * @return
	 */
	List<UserLightBean> retrieveAllUsers();
	
	/**
	 * Retrieve the user by code.<br/>
	 * 
	 * @param userCode
	 * @return
	 */
	UserBean retrieveUserByCode(final String userCode);

	/**
	 * Insert the user bean.<br/>
	 * 
	 * @param userBean
	 * @return
	 */
	Integer insertUserBean(final UserBean userBean);
	
	
	/**
	 * Update the user bean using the userId.<br/>
	 * 
	 * @param userBean
	 * @param userId
	 */
	void updateUserBean(final UserBean userBean , final Integer userId);
	
	/**
	 * Insert the role user bean.<br/>
	 * 
	 * @param userId
	 * @param roleId
	 */
	void insertRoleUserBean(final Integer userId , final Integer roleId);
	
	/**
	 * Delete all the roles for a userBean.<br/>
	 * 
	 * @param userId
	 */
	void deleteRolesUserBean(final Integer userId);
	
	
	/**
	 * Delete the user using his id.<br/>
	 * 
	 * @param userId
	 */
	void deleteUserBean(final Integer userId);
	
}
