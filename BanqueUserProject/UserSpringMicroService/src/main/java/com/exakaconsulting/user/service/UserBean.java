package com.exakaconsulting.user.service;

import java.util.List;
import java.util.Locale;

public class UserBean extends UserLightBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2672247197645530438L;
	
	private Locale locale;

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
