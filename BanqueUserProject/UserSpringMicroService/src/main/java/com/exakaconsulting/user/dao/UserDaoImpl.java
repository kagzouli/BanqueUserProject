package com.exakaconsulting.user.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import static com.exakaconsulting.IConstantUserApplication.USER_DATASOURCE_BEAN;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.exakaconsulting.exception.TechnicalException;
import com.exakaconsulting.user.service.UserBean;
import com.exakaconsulting.user.service.UserLightBean;

@Repository
public class UserDaoImpl implements IUserDao{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private static final String ALL_USERS_SQL = "select userId, userIdentifier,firstName,lastName from USER_TABLE order by userIdentifier asc";

	private static final String USER_BY_CODE_SQL = "select userId, userIdentifier,firstName,lastName,locale from USER_TABLE WHERE userIdentifier = :userIdentifierParam";

	private static final String INSERT_USER_SQL = "INSERT INTO USER_TABLE(userIdentifier,firstName,lastName,locale) VALUES (:userIdentifierParam, :firstNameParam, :lastNameParam, :localeParam)";

	private static final String UPDATE_USER_SQL = "UPDATE USER_TABLE SET userIdentifier = :userIdentifierParam , firstName = :firstNameParam , lastName = :lastNameParam ,locale = :localeParam WHERE userId = :userIdParam";

	private static final String DELETE_USER_SQL = "DELETE FROM USER_TABLE WHERE userId = :userIdParam";
	
	private static final String INSERT_USERROLE_SQL = "INSERT INTO USER_ROLE_TABLE(userId, roleId) values (:userIdParam, :roleIdParam)";
	
	private static final String DELETE_USER_ROLE_BYIDUSER_SQL = "DELETE FROM USER_ROLE_TABLE where userId = :userIdParam";
	
	@Autowired
	@Qualifier(USER_DATASOURCE_BEAN)
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	

	@Override
	public List<UserLightBean> retrieveAllUsers() {

		List<UserLightBean> listUserBean = Collections.<UserLightBean>emptyList();
		
		try{
			listUserBean = jdbcTemplate.query(ALL_USERS_SQL, new HashMap<>(), new UserLightRowMapper());
		}catch(Exception exception){
			LOGGER.error(exception.getMessage() , exception);
			throw new TechnicalException(exception.getMessage() , exception);
		}
					
		return listUserBean;
	}

	@Override
	public UserBean retrieveUserByCode(String userCode) {
		UserBean userBean = null;
		try{
			Map<String, String> params = new HashMap<>();
			params.put("userIdentifierParam", userCode);
			userBean = jdbcTemplate.queryForObject(USER_BY_CODE_SQL, params, new UserRowMapper());
		}catch(EmptyResultDataAccessException exception){
			LOGGER.warn("No user for the code : " + userCode);
		}catch(Exception exception){
			LOGGER.error(exception.getMessage() , exception);
			throw new TechnicalException(exception.getMessage() , exception);
		}
		return userBean;
	}
	
	@Override
	public Integer insertUserBean(UserBean userBean) {
		Assert.notNull(userBean, "userBean must be set");
		Integer userIdCreated = 0;
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("userIdentifierParam", userBean.getIdentifierCodeUser());
			params.put("firstNameParam", userBean.getFirstName());
			params.put("lastNameParam", userBean.getLastName());
			params.put("localeParam", StringUtils.upperCase(userBean.getLocale()));

			SqlParameterSource paramSource = new MapSqlParameterSource(params);

			KeyHolder keyHolder=new GeneratedKeyHolder();
			Integer returnValue =  this.jdbcTemplate.update(INSERT_USER_SQL, paramSource , keyHolder);

			if (returnValue <= 0) {
				throw new TechnicalException("No insert has been done for user");
			}else{
				userIdCreated = keyHolder.getKey().intValue();
			}
		} catch (TechnicalException exception) {
			throw exception;
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception.getMessage());
		}
		
		return userIdCreated;
	}


	@Override
	public void insertRoleUserBean(Integer userId, Integer roleId) {
		Assert.notNull(userId, "userId must be set");
		Assert.notNull(roleId, "roleId must be set");
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("userIdParam", userId);
			params.put("roleIdParam", roleId);

			Integer returnValue = this.jdbcTemplate.update(INSERT_USERROLE_SQL, params);

			if (returnValue <= 0) {
				throw new TechnicalException("No insert has been done for userRole");
			}
		} catch (TechnicalException exception) {
			throw exception;
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception.getMessage());
		}		
	}


	@Override
	public void deleteRolesUserBean(Integer userId) {
		Assert.notNull(userId, "userId must be set");
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("userIdParam", userId);

			this.jdbcTemplate.update(DELETE_USER_ROLE_BYIDUSER_SQL, params);

		} catch (TechnicalException exception) {
			throw exception;
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception.getMessage());
		}		
		
	}


	@Override
	public void updateUserBean(UserBean userBean, Integer userId) {
		Assert.notNull(userBean, "userBean must be set");
		Assert.notNull(userId, "The user id must be set");
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("userIdParam", userId);
			params.put("userIdentifierParam", userBean.getIdentifierCodeUser());
			params.put("firstNameParam", userBean.getFirstName());
			params.put("lastNameParam", userBean.getLastName());
			params.put("localeParam", userBean.getLocale());

			Integer returnValue =  this.jdbcTemplate.update(UPDATE_USER_SQL, params);

			if (returnValue <= 0) {
				throw new TechnicalException("No update has been done for user");
			}
		} catch (TechnicalException exception) {
			throw exception;
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception.getMessage());
		}
	}


	@Override
	public void deleteUserBean(Integer userId) {
		Assert.notNull(userId, "The user id must be set");
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("userIdParam", userId);

			Integer returnValue =  this.jdbcTemplate.update(DELETE_USER_SQL, params);

			if (returnValue <= 0) {
				throw new TechnicalException("No update has been done for user");
			}
		} catch (TechnicalException exception) {
			throw exception;
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception.getMessage());
		}		
	}		
}
