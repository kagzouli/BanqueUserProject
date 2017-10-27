package com.exakaconsulting.user.web;

import static com.exakaconsulting.IConstantUserApplication.TECHNICAL_EXCEPTION;
import static com.exakaconsulting.IConstantUserApplication.USERS_LIST_REST;
import static com.exakaconsulting.IConstantUserApplication.ROLES_LIST_REST;
import static com.exakaconsulting.IConstantUserApplication.INSERT_USER_REST;
import static com.exakaconsulting.IConstantUserApplication.UPDATE_USER_REST;
import static com.exakaconsulting.IConstantUserApplication.DELETE_USER_REST;
import static com.exakaconsulting.IConstantUserApplication.USER_SERVICE;
import static com.exakaconsulting.IConstantUserApplication.USERS_BYCODE_REST;

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
import com.exakaconsulting.user.service.IUserService;
import com.exakaconsulting.user.service.RoleBean;
import com.exakaconsulting.user.service.UserAlreadyExistsException;
import com.exakaconsulting.user.service.UserBean;
import com.exakaconsulting.user.service.UserLightBean;
import com.exakaconsulting.user.service.UserNotFoundException;

@RestController
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	@Qualifier(USER_SERVICE)
	private IUserService userService;

	@RequestMapping(value = "/users", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView home() {
		ModelAndView modelView = new ModelAndView("userViewsHome");

		modelView.addObject("TEST", " USER");

		return modelView;
	}

	@RequestMapping(value = USERS_LIST_REST, method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('useradministrator') OR hasRole('userbank')")
	public JsonResult<List<UserLightBean>> retrieveUsersList() {

		LOGGER.info("BEGIN of the method retrieveUsersList of the class " + UserController.class.getName());

		JsonResult<List<UserLightBean>> jsonResult = new JsonResult<>();
		try {
			final List<UserLightBean> usersList = userService.retrieveUsersList();
			jsonResult.setResult(usersList);
			jsonResult.setSuccess(true);
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(TECHNICAL_EXCEPTION);
		}
		LOGGER.info("END of the method retrieveUsersList of the class " + UserController.class.getName());

		return jsonResult;
	}

	@RequestMapping(value = USERS_BYCODE_REST, method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('useradministrator') OR hasRole('userbank')")
	public JsonResult<UserBean> retrieveUserByCode(@RequestParam(name = "userCode", required = true) final String userCode) {
		LOGGER.info("BEGIN of the method retrieveUserByCode of the class " + UserController.class.getName()
				+ "with the parameter userCode = " + userCode); 

		JsonResult<UserBean> jsonResult = new JsonResult<>();
		try {
			final UserBean userBean = userService.retrieveUserByCode(userCode);
			jsonResult.setResult(userBean);
			jsonResult.setSuccess(true);
		} catch (UserNotFoundException exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(exception.getMessage());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(TECHNICAL_EXCEPTION);
		}
		LOGGER.info("END of the method retrieveUserByCode of the class " + UserController.class.getName()
				+ "with the parameter userCode = " + userCode);

		return jsonResult;
	}

	@RequestMapping(value = ROLES_LIST_REST, method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('useradministrator')")
	public JsonResult<List<RoleBean>> retrieveRolesList() {

		LOGGER.info("BEGIN of the method retrieveRolesList of the class " + UserController.class.getName());

		JsonResult<List<RoleBean>> jsonResult = new JsonResult<>();
		try {
			final List<RoleBean> rolesList = userService.retrieveRolesList();
			jsonResult.setResult(rolesList);
			jsonResult.setSuccess(true);
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(TECHNICAL_EXCEPTION);
		}
		LOGGER.info("END of the method retrieveRolesList of the class " + UserController.class.getName());

		return jsonResult;
	}

	@RequestMapping(value = INSERT_USER_REST, method = { RequestMethod.GET, RequestMethod.POST }, produces = {
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
			jsonResult.addError(TECHNICAL_EXCEPTION);
		}
		LOGGER.info("END of the method insertUser of the class " + UserController.class.getName());

		return jsonResult;
	}

	@RequestMapping(value = UPDATE_USER_REST, method = { RequestMethod.GET, RequestMethod.POST }, produces = {
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
			jsonResult.addError(TECHNICAL_EXCEPTION);
		}
		LOGGER.info("END of the method updateUser of the class " + UserController.class.getName());

		return jsonResult;
	}
	
	
	@RequestMapping(value = DELETE_USER_REST, method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('useradministrator')")
	public JsonResult<Object> deleteUser(@RequestParam(name = "userCode", required = true) final String userCode) {
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
			jsonResult.addError(TECHNICAL_EXCEPTION);
		}
		LOGGER.info("END of the method deleteUser of the class " + UserController.class.getName()
				+ "with the parameter userCode = " + userCode);

		return jsonResult;
	}


}
