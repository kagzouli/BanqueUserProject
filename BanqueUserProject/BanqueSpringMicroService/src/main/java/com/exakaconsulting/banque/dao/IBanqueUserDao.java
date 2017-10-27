package com.exakaconsulting.banque.dao;

import com.exakaconsulting.banque.service.BanqueUserBean;
import com.exakaconsulting.banque.service.UserBanqueNotFoundException;

/**
 * Class information to call user using micro service
 * 
 * @author Toto
 *
 */
public interface IBanqueUserDao {
	
	/**
	 * Method to retrieve a user using an identifier.<br/>
	 * 
	 * @param userIdentifier
	 * @return
	 * @throws UserBanqueNotFoundException
	 */
	BanqueUserBean retrieveUserByIdentifier(final String userCode) throws UserBanqueNotFoundException;

}
