package com.exakaconsulting.user.service;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

public class UserLightBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1416970217467499972L;
	
	@JsonIgnore
	@ApiModelProperty(value="The user id.")
	protected Integer userId;
	
	@ApiModelProperty(value= "The identifier code of the user.", required=true)
	protected String identifierCodeUser;
	
	@ApiModelProperty(value= "The first name of the user.", required=true)
	protected String firstName;
	
	@ApiModelProperty(value="The last name of the user.", required=true)
	protected String lastName;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

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
}
