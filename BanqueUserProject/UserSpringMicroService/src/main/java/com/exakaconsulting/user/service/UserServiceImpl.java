package com.exakaconsulting.user.service;

import static com.exakaconsulting.IConstantUserApplication.USER_SERVICE;
import static com.exakaconsulting.IConstantUserApplication.TRANSACTIONAL_USER_BEAN;
import static com.exakaconsulting.IConstantUserApplication.USER_NOT_FOUND_EXCEPTION;
import static com.exakaconsulting.IConstantUserApplication.USER_ALREADY_EXISTS_EXCEPTION;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.exakaconsulting.exception.TechnicalException;
import com.exakaconsulting.user.dao.IRoleDao;
import com.exakaconsulting.user.dao.IUserDao;

@Service(USER_SERVICE)
public class UserServiceImpl implements IUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private IUserDao userDao;

	@Autowired
	private IRoleDao roleDao;

	@Override
	public List<RoleBean> retrieveRolesList() {
		LOGGER.info("BEGIN of the method retrieveRolesList of the class " + UserServiceImpl.class.getName());

		List<RoleBean> listRoles = Collections.<RoleBean>emptyList();
		try {
			listRoles = roleDao.retrieveAllRoles();
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}

		LOGGER.info("END of the method retrieveRolesList of the class " + UserServiceImpl.class.getName());
		return listRoles;
	}

	@Override
	public List<UserLightBean> retrieveUsersList() {

		LOGGER.info("BEGIN of the method retrieveUsersList of the class " + UserServiceImpl.class.getName());

		List<UserLightBean> listUsers = Collections.<UserLightBean>emptyList();
		try {
			listUsers = userDao.retrieveAllUsers();
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}

		LOGGER.info("END of the method retrieveUsersList of the class " + UserServiceImpl.class.getName());
		return listUsers;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED, transactionManager = TRANSACTIONAL_USER_BEAN)
	public void updateUser(UserBean userBean) throws UserNotFoundException{
		LOGGER.info("BEGIN of the method updateUser of the class " + UserServiceImpl.class.getName());

		Assert.notNull(userBean);
		try {
			//The user must exist if we want to update it.
			final UserBean userBeanInDb = userDao.retrieveUserByCode(userBean.getIdentifierCodeUser());
			if (userBeanInDb == null) {
				throw new UserNotFoundException(USER_NOT_FOUND_EXCEPTION , userBean.getIdentifierCodeUser());
			}

			// Update the user
			final Integer userIdUpdate = userBeanInDb.getUserId();
			userDao.updateUserBean(userBean , userIdUpdate);
			userBean.setUserId(userBeanInDb.getUserId());
			
			// Delete all the old role for the user
			userDao.deleteRolesUserBean(userIdUpdate);

			// Insert the list of new roles
			this.insertListRolesUser(userBean.getListRoles(), userIdUpdate);

		} catch (UserNotFoundException exception) {
			throw exception;
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method updateUser of the class " + UserServiceImpl.class.getName());
	}

	@Override
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED, transactionManager = TRANSACTIONAL_USER_BEAN)
	public Integer insertUser(UserBean userBean) throws UserAlreadyExistsException {
		LOGGER.info("BEGIN of the method insertUser of the class " + UserServiceImpl.class.getName());

		Assert.notNull(userBean);
		Integer userIdCreated = null;
		try {
			final UserBean userBeanInDb = userDao.retrieveUserByCode(userBean.getIdentifierCodeUser());
			if (userBeanInDb != null) {
				throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_EXCEPTION);
			}

			userIdCreated = userDao.insertUserBean(userBean);
			userBean.setUserId(userIdCreated);

			this.insertListRolesUser(userBean.getListRoles(), userIdCreated);

		} catch (UserAlreadyExistsException exception) {
			throw exception;
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method insertUser of the class " + UserServiceImpl.class.getName());
		return userIdCreated;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED, transactionManager = TRANSACTIONAL_USER_BEAN)
	public void deleteUser(final String userCode) throws UserNotFoundException {
		LOGGER.info("BEGIN of the method deleteUser of the class " + UserServiceImpl.class.getName() + " with the parameter userCode = '" + userCode + "'");

		Assert.hasLength(userCode , "The user code must be set");
		try {
			//The user must exist if we want to update it.
			final UserBean userBeanInDb = userDao.retrieveUserByCode(userCode);
			if (userBeanInDb == null) {
				throw new UserNotFoundException(USER_NOT_FOUND_EXCEPTION , userCode);
			}

			// Retrieve the user id delete.
			final Integer userIdDelete = userBeanInDb.getUserId();

			// Delete all the old role for the user
			userDao.deleteRolesUserBean(userIdDelete);

			// Delete the user
			userDao.deleteUserBean(userIdDelete);
			


		} catch (UserNotFoundException exception) {
			throw exception;
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method deleteUser of the class " + UserServiceImpl.class.getName()  + " with the parameter userCode = '" + userCode + "'");

	}

	@Override
	public UserBean retrieveUserByCode(String userCode) throws UserNotFoundException {
		LOGGER.info("BEGIN of the method retrieveUserByCode of the class " + UserServiceImpl.class.getName()
				+ " with the code : " + userCode);

		Assert.hasLength(userCode, "User code has to be set");

		UserBean userBean = null;
		try {
			userBean = userDao.retrieveUserByCode(userCode);

			if (userBean != null) {
				List<RoleBean> listRoles = roleDao.retrieveListRolesByUser(userBean.getUserId());
				userBean.setListRoles(listRoles);
			} else {
				throw new UserNotFoundException(USER_NOT_FOUND_EXCEPTION, userCode);
			}

		} catch (UserNotFoundException exception) {
			throw exception;
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}

		LOGGER.info("END of the method retrieveUserByCode of the class " + UserServiceImpl.class.getName()
				+ " with the code : " + userCode);

		return userBean;
	}

	/**
	 * Method to update list roles user.<br/>
	 * 
	 * @param listRoles
	 * @param userId
	 * @return
	 */
	private void insertListRolesUser(List<RoleBean> listRoles, final Integer userId) {
		if (listRoles != null) {
			// Put all the roles in a Map.<br/>
			Map<String, Integer> mapRoles = roleDao.retrieveMapRoles();
			for (RoleBean roleBean : listRoles) {
				roleBean.setRoleId(mapRoles.get(roleBean.getRoleCode()));
				userDao.insertRoleUserBean(userId, roleBean.getRoleId());
			}
		}
	}

}
