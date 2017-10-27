package com.exakaconsulting.user.service;

import java.util.List;
import java.util.Locale;

import io.swagger.annotations.ApiModelProperty;

public class UserBean extends UserLightBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2672247197645530438L;
	
	@ApiModelProperty(value="The locale of the user.", required=true)
	private Locale locale;

	@ApiModelProperty(value="The roles list of the user.")
	private List<RoleBean> listRoles;

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public List<RoleBean> getListRoles() {
		return listRoles;
	}

	public void setListRoles(List<RoleBean> listRoles) {
		this.listRoles = listRoles;
	}
	
}
