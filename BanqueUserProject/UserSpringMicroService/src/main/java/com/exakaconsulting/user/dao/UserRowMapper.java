package com.exakaconsulting.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.exakaconsulting.user.service.UserBean;

public class UserRowMapper implements RowMapper<UserBean>{

	@Override
	public UserBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserBean userBean = new UserBean();
		userBean.setUserId(rs.getInt("userId"));
		userBean.setIdentifierCodeUser(rs.getString("userIdentifier"));
		userBean.setFirstName(rs.getString("firstName"));
		userBean.setLastName(rs.getString("lastName"));
		userBean.setLocale(rs.getString("locale"));
		return userBean;
	}
	

}
