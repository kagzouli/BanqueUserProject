package com.exakaconsulting.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.exakaconsulting.user.service.RoleBean;

public class RoleRowMapper implements RowMapper<RoleBean>{

	@Override
	public RoleBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		RoleBean roleBean = new RoleBean();
		roleBean.setRoleId(rs.getInt("roleId"));
		roleBean.setRoleCode(rs.getString("roleCode"));
		return roleBean;
	}

}
