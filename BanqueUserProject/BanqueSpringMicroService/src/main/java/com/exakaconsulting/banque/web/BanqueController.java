package com.exakaconsulting.banque.web;

import static com.exakaconsulting.IConstantApplication.BALANCE_USER_REST;
import static com.exakaconsulting.IConstantApplication.BANQUE_SERVICE;
import static com.exakaconsulting.IConstantApplication.CREDIT_ACCOUNT_REST;
import static com.exakaconsulting.IConstantApplication.DEBIT_ACCOUNT_REST;
import static com.exakaconsulting.IConstantApplication.LIST_OPERATION_REST;

import java.math.BigDecimal;
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
import com.exakaconsulting.banque.service.AccountOperationBean;
import com.exakaconsulting.banque.service.IBanqueService;
import com.exakaconsulting.banque.service.MaxAmountCreditException;
import com.exakaconsulting.banque.service.NegativeBalanceAmountException;
import com.exakaconsulting.banque.service.UserBanqueNotFoundException;
import com.exakaconsulting.exception.TechnicalException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "/", description = "This REST API is use to make actions on account user.<br/>")
public class BanqueController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BanqueController.class);

	@Autowired
	@Qualifier(BANQUE_SERVICE)
	private IBanqueService banqueService;
	
	@ApiOperation(value = "This method is use to retrieve the home of the bank", response = ModelAndView.class)
	@RequestMapping(value = "/banque", method = { RequestMethod.GET })
	public ModelAndView home() {
		ModelAndView modelView = new ModelAndView("banqueViewsHome");

		modelView.addObject("TEST", "KARIM");

		return modelView;

	}

	@ApiOperation(value = "This method is to retrieve the list of operations by a user between 2 dates", response = JsonResult.class , notes="JsonResult of AccountOperationBean")
	@RequestMapping(value = LIST_OPERATION_REST, method = { RequestMethod.POST}, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('bankmanager') OR hasRole('bankcollaborator')")
	public JsonResult<List<AccountOperationBean>> listOperations(@ApiParam(value="The account operation", required=true) @RequestBody(required=true) final AccountOperationStateParam accountOperationStateParam) {
		LOGGER.info("BEGIN of the method listOperations of the class " + BanqueController.class.getName()
				+ "identifierUser : " + accountOperationStateParam.getUserIdentifier() + " , Begin date : "
				+ accountOperationStateParam.getBeginDate() + " , End Date : "
				+ accountOperationStateParam.getEndDate());

		JsonResult<List<AccountOperationBean>> jsonResult = new JsonResult<>();
		try {
			List<AccountOperationBean> listOperationAccount = banqueService.retrieveOperations(
					accountOperationStateParam.getUserIdentifier(), accountOperationStateParam.getBeginDate(),
					accountOperationStateParam.getEndDate());
			jsonResult.setResult(listOperationAccount);
			jsonResult.setSuccess(true);
		} catch (UserBanqueNotFoundException exception) {
			jsonResult.addError(exception.getMessage());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method listOperations of the class " + BanqueController.class.getName()
				+ "identifierUser : " + accountOperationStateParam.getUserIdentifier() + " , Begin date : "
				+ accountOperationStateParam.getBeginDate() + " , End Date : "
				+ accountOperationStateParam.getEndDate());

		return jsonResult;
	}

	/**
	 * credit Account for user.<br/>
	 * 
	 */
	@ApiOperation(value = "This method credits the account of the user with the amount in input.", response = JsonResult.class)
	@RequestMapping(value = CREDIT_ACCOUNT_REST, method = { RequestMethod.POST }, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('bankmanager')")
	public JsonResult<Object> creditAccount(@ApiParam(value="The credit operation on account") @RequestBody(required=true) final OperationUserParam operationUserParam) {

		LOGGER.info("BEGIN of the method creditAccount of the class " + BanqueController.class.getName()
				+ " ( userIdentifier = " + operationUserParam.getIdentifierUser() + " , labelOperation  = "
				+ operationUserParam.getLabelOperation() + " )");

		JsonResult<Object> jsonResult = new JsonResult<>();

		try {
			banqueService.creditAmount(operationUserParam.getIdentifierUser(), operationUserParam.getLabelOperation(),
					operationUserParam.getAmount());
			jsonResult.setSuccess(true);
		} catch (UserBanqueNotFoundException | MaxAmountCreditException exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(exception.getMessage());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}

		LOGGER.info("END of the method creditAccount of the class " + BanqueController.class.getName()
				+ " ( userIdentifier = " + operationUserParam.getIdentifierUser() + " , labelOperation  = "
				+ operationUserParam.getLabelOperation() + " )");

		return jsonResult;
	}

	@ApiOperation(value = "This method debits the account of the user with the amount in input.", response = JsonResult.class , notes="JsonResult")
	@RequestMapping(value = DEBIT_ACCOUNT_REST, method = { RequestMethod.POST }, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('bankmanager')")
	public JsonResult<BigDecimal> debitAccount(@ApiParam(value="The debit operation on account")  @RequestBody(required=true) final OperationUserParam operationUserParam) {

		LOGGER.info("BEGIN of the method debitAccount of the class " + BanqueController.class.getName()
				+ " ( userIdentifier = " + operationUserParam.getIdentifierUser() + " , labelOperation  = "
				+ operationUserParam.getLabelOperation() + " )");

		JsonResult<BigDecimal> jsonResult = new JsonResult<>();
		try {
			final BigDecimal balanceAmount = banqueService.debitAmount(operationUserParam.getIdentifierUser(),
					operationUserParam.getLabelOperation(), operationUserParam.getAmount());
			jsonResult.setResult(balanceAmount);
			jsonResult.setSuccess(true);
		} catch (UserBanqueNotFoundException | NegativeBalanceAmountException exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(exception.getMessage());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}

		LOGGER.info("END of the method debitAccount of the class " + BanqueController.class.getName()
				+ " ( userIdentifier = " + operationUserParam.getIdentifierUser() + " , labelOperation  = "
				+ operationUserParam.getLabelOperation() + " )");

		return jsonResult;
	}

	@ApiOperation(value = "This method retrieve the balance of the user", response = JsonResult.class , notes="JsonResult of BigDecimal")
	@RequestMapping(value = BALANCE_USER_REST, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('bankmanager') OR hasRole('bankcollaborator')")
	public JsonResult<BigDecimal> retrieveBalanceAccountUser(
			@ApiParam(value = "The user identifier for the search.", required=true) @RequestParam(name = "userIdentifier", required = true) final String userIdentifier) {

		LOGGER.info("BEGIN of the method debitAccount of the class " + BanqueController.class.getName() + " ( userIdentifier = " + userIdentifier + " ) ");
		
		JsonResult<BigDecimal> jsonResult = new JsonResult<>();
		try {
			final BigDecimal sumAccountUser = banqueService.retrieveBalanceAmount(userIdentifier);
			jsonResult.setResult(sumAccountUser);
			jsonResult.setSuccess(true);
		} catch (UserBanqueNotFoundException exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(exception.getMessage());
		}catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method debitAccount of the class " + BanqueController.class.getName() + " ( userIdentifier = " + userIdentifier + " ) ");

		return jsonResult;
	}
}
