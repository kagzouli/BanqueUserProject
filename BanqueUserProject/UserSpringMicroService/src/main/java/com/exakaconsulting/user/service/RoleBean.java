package com.exakaconsulting.user.service;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

public class RoleBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3455731874854820243L;
	
	@JsonIgnore
	private Integer roleId;
	
	@ApiModelProperty(value="The role code" , required=true)
	private String roleCode;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

}
