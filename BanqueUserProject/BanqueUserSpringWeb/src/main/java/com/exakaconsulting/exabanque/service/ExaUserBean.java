package com.exakaconsulting.exabanque.service;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

public class ExaUserBean implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="The identifier code user.", required=true)
	protected String identifierCodeUser;
	
	@ApiModelProperty(value="The first name user.", required=true)
	protected String firstName;
	
	@ApiModelProperty(value="The last name user.", required=true)
	protected String lastName;
	
	@ApiModelProperty(value="The entire name user.")
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
