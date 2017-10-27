package com.exakaconsulting.user.dao;

import static com.exakaconsulting.IConstantUserApplication.USER_DATASOURCE_BEAN;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.exakaconsulting.exception.TechnicalException;
import com.exakaconsulting.user.service.RoleBean;

@Repository
public class RoleDaoImpl implements IRoleDao{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	private NamedParameterJdbcTemplate jdbcTemplate;
	
	/** All roles by user  */
	private static final String ALL_ROLES_BY_USERID_SQL = "select ROLE_TABLE.roleId, ROLE_TABLE.roleCode from USER_ROLE_TABLE, ROLE_TABLE WHERE USER_ROLE_TABLE.roleId = ROLE_TABLE.roleId and USER_ROLE_TABLE.userId = :userIdParam order by ROLE_TABLE.roleCode asc";

	/** All roles  **/
	protected static final String ALL_ROLES_SQL = "select roleId, roleCode from ROLE_TABLE order by roleCode asc";
	
	@Autowired
	@Qualifier(USER_DATASOURCE_BEAN)
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public List<RoleBean> retrieveListRolesByUser(Integer userId) {
		List<RoleBean> listRolesBean = Collections.<RoleBean>emptyList();
		
		Map<String, Object> params = new HashMap<>();
		params.put("userIdParam", userId);
		try{
			listRolesBean = jdbcTemplate.query(ALL_ROLES_BY_USERID_SQL, params, new RoleRowMapper());
		}catch(Exception exception){
			LOGGER.error(exception.getMessage() , exception);
			throw new TechnicalException(exception.getMessage() , exception);
		}
					
		return listRolesBean;

	}

	@Override
	public List<RoleBean> retrieveAllRoles() {
		List<RoleBean> listRolesBean = Collections.<RoleBean>emptyList();
		
		try{
			listRolesBean = jdbcTemplate.query(ALL_ROLES_SQL, new HashMap<>(), new RoleRowMapper());
		}catch(Exception exception){
			LOGGER.error(exception.getMessage() , exception);
			throw new TechnicalException(exception.getMessage() , exception);
		}
					
		return listRolesBean;
	}
	
	public Map<String, Integer> retrieveMapRoles(){
		List<RoleBean> listAllRoles = this.retrieveAllRoles();
		
		Map<String, Integer> mapRoles = new HashMap<>();
		listAllRoles.stream().forEach(roleBean -> mapRoles.put(roleBean.getRoleCode() , roleBean.getRoleId()));

		return mapRoles;
	}

}
