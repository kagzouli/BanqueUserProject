package com.exakaconsulting.exabanque.service;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ExaUserBean implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String identifierCodeUser;
	
	protected String firstName;
	
	protected String lastName;
	
	protected String entireName;
	
	@JsonIgnore
	private List<String> roles;

	public String getIdentifierCodeUser() {
		return identifierCodeUser;
	}

	public void setIdentifierCodeUser(String identifierCodeUser) {
		this.identifierCodeUser = identifierCodeUser;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public String getEntireName(){
		return this.firstName + " " + this.lastName;
	}
	
	public void setEntireName(final String entireName){
		this.entireName = entireName;
	}
	
}
