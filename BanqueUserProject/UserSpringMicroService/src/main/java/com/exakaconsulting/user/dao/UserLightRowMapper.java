package com.exakaconsulting.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.exakaconsulting.user.service.UserLightBean;

public class UserLightRowMapper implements RowMapper<UserLightBean>{

	@Override
	public UserLightBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserLightBean userLightBean = new UserLightBean();
		userLightBean.setUserId(rs.getInt("userId"));
		userLightBean.setIdentifierCodeUser(rs.getString("userIdentifier"));
		userLightBean.setFirstName(rs.getString("firstName"));
		userLightBean.setLastName(rs.getString("lastName"));
		return userLightBean;
	}
	

}
