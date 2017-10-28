package com.exakaconsulting.user.web;

import static com.exakaconsulting.IConstantUserApplication.USERS_LIST_REST;
import static com.exakaconsulting.IConstantUserApplication.ROLES_LIST_REST;
import static com.exakaconsulting.IConstantUserApplication.INSERT_USER_REST;
import static com.exakaconsulting.IConstantUserApplication.UPDATE_USER_REST;
import static com.exakaconsulting.IConstantUserApplication.DELETE_USER_REST;
import static com.exakaconsulting.IConstantUserApplication.USER_SERVICE;
import static com.exakaconsulting.IConstantUserApplication.USERS_BYCODE_REST;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.exakaconsulting.JsonResult;
import com.exakaconsulting.exception.TechnicalException;
import com.exakaconsulting.user.service.IUserService;
import com.exakaconsulting.user.service.RoleBean;
import com.exakaconsulting.user.service.UserAlreadyExistsException;
import com.exakaconsulting.user.service.UserBean;
import com.exakaconsulting.user.service.UserLightBean;
import com.exakaconsulting.user.service.UserNotFoundException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "/", description = "This REST API is use to have informations and do action on user.<br/>")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	@Qualifier(USER_SERVICE)
	private IUserService userService;

	@ApiOperation(value = "This method is use to retrieve the list page of users", response = ModelAndView.class)
	@RequestMapping(value = "/users", method = { RequestMethod.GET })
	public ModelAndView home() {
		ModelAndView modelView = new ModelAndView("userViewsHome");

		modelView.addObject("TEST", " USER");

		return modelView;
	}

	@ApiOperation(value = "This method is use to retrieve user list", response = UserLightBean.class, responseContainer = "List")
	@RequestMapping(value = USERS_LIST_REST, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('useradministrator') OR hasRole('userbank')")	
	public List<UserLightBean> retrieveUsersList() {

		LOGGER.info("BEGIN of the method retrieveUsersList of the class " + UserController.class.getName());

		List<UserLightBean> usersList = Collections.emptyList();
		try {
			usersList = userService.retrieveUsersList();
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method retrieveUsersList of the class " + UserController.class.getName());

		return usersList;
	}

	@ApiOperation(value = "This method is use to retrieve a user by the code.", response = UserBean.class)
	@RequestMapping(value = USERS_BYCODE_REST, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes={MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	@ResponseBody
	@PreAuthorize("hasRole('useradministrator') OR hasRole('userbank')")
	public UserBean retrieveUserByCode(@ApiParam(value = "The user code to search.", required=true) @RequestParam(name = "userCode", required = true) final String userCode) {
		LOGGER.info("BEGIN of the method retrieveUserByCode of the class " + UserController.class.getName()
				+ "with the parameter userCode = " + userCode); 

		UserBean userBean = null;
		try {
			userBean = userService.retrieveUserByCode(userCode);
		} catch (UserNotFoundException exception) {
			LOGGER.error(exception.getMessage(), exception);
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method retrieveUserByCode of the class " + UserController.class.getName()
				+ "with the parameter userCode = " + userCode);

		return userBean;
	}

	@ApiOperation(value = "This method is use to retrieve the roles list in the application.", response = RoleBean.class , responseContainer="List")
	@RequestMapping(value = ROLES_LIST_REST, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('useradministrator')")
	public List<RoleBean> retrieveRolesList() {

		LOGGER.info("BEGIN of the method retrieveRolesList of the class " + UserController.class.getName());

		List<RoleBean> rolesList = Collections.emptyList();
		try {
			rolesList = userService.retrieveRolesList();
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method retrieveRolesList of the class " + UserController.class.getName());

		return rolesList;
	}

	@ApiOperation(value = "This method is use to insert a user in the application.", response = JsonResult.class)
	@RequestMapping(value = INSERT_USER_REST, method = {  RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('useradministrator')")
	public JsonResult<Object> insertUser(@RequestBody UserBean userBean) {

		LOGGER.info("BEGIN of the method insertUser of the class " + UserController.class.getName());

		JsonResult<Object> jsonResult = new JsonResult<>();
		try {
			userService.insertUser(userBean);
			jsonResult.setSuccess(true);
		} catch (UserAlreadyExistsException exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(exception.getMessage());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method insertUser of the class " + UserController.class.getName());

		return jsonResult;
	}

	@ApiOperation(value = "This method is use to update a user in the application.", response = JsonResult.class)
	@RequestMapping(value = UPDATE_USER_REST, method = { RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('useradministrator')")
	public JsonResult<Object> updateUser(@RequestBody UserBean userBean) {

		LOGGER.info("BEGIN of the method updateUser of the class " + UserController.class.getName());

		JsonResult<Object> jsonResult = new JsonResult<>();
		try {
			userService.updateUser(userBean);
			jsonResult.setSuccess(true);
		} catch (UserNotFoundException exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(exception.getMessage());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method updateUser of the class " + UserController.class.getName());

		return jsonResult;
	}
	
	@ApiOperation(value = "This method is use to delete a user in the application.", response = JsonResult.class)
	@RequestMapping(value = DELETE_USER_REST, method = { RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('useradministrator')")
	public JsonResult<Object> deleteUser(@ApiParam(value = "The user code to delete.", required=true) @RequestParam(name = "userCode", required = true) final String userCode) {
		LOGGER.info("BEGIN of the method deleteUser of the class " + UserController.class.getName()
				+ "with the parameter userCode = " + userCode);

		JsonResult<Object> jsonResult = new JsonResult<>();
		try {
			userService.deleteUser(userCode);
			jsonResult.setSuccess(true);
		} catch (UserNotFoundException exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(exception.getMessage());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method deleteUser of the class " + UserController.class.getName()
				+ "with the parameter userCode = " + userCode);

		return jsonResult;
	}


}
