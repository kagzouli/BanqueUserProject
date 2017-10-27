package com.exakaconsulting.user.dao;

import java.util.List;
import java.util.Map;

import com.exakaconsulting.user.service.RoleBean;

public interface IRoleDao {	/**
	 * 
	 * Identifier list roles by userId.<br/>
	 * 
	 * @param userId Id user.<br/>
	 * @return Return the list of roles associated to the userId.<br/>
	 */
	List<RoleBean> retrieveListRolesByUser(final Integer userId);
	
	
	/**
	 * Method to retrieve all the roles.<br/>
	 * 
	 * @return
	 */
	List<RoleBean> retrieveAllRoles();
	
	
	/**
	 * Retrieve map of all the roles - key = code / value = id.<br/>
	 * 
	 * @return
	 */
	Map<String, Integer> retrieveMapRoles();

	
	

}
