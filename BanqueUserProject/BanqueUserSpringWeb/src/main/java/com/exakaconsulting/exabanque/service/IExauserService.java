package com.exakaconsulting.exabanque.service;

import java.util.List;

public interface IExauserService {
	
	/**
	 * Retrieve the list of users.<br/>
	 * 
	 * @return Return the list of users.<br/>
	 */
	List<ExaUserBean> retrieveUsersList();


}
