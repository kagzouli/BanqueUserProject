package com.exakaconsulting.banque.web;

import static com.exakaconsulting.IConstantApplication.BALANCE_USER_REST;
import static com.exakaconsulting.IConstantApplication.BANQUE_SERVICE;
import static com.exakaconsulting.IConstantApplication.CREDIT_ACCOUNT_REST;
import static com.exakaconsulting.IConstantApplication.DEBIT_ACCOUNT_REST;
import static com.exakaconsulting.IConstantApplication.LIST_OPERATION_REST;
import static com.exakaconsulting.IConstantApplication.TECHNICAL_EXCEPTION;

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

@RestController
public class BanqueController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BanqueController.class);

	@Autowired
	@Qualifier(BANQUE_SERVICE)
	private IBanqueService banqueService;
	
	@RequestMapping(value = "/banque", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView home() {
		ModelAndView modelView = new ModelAndView("banqueViewsHome");

		modelView.addObject("TEST", "KARIM");

		return modelView;

	}

	@RequestMapping(value = LIST_OPERATION_REST, method = { RequestMethod.GET, RequestMethod.POST }, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('bankmanager') OR hasRole('bankcollaborator')")
	public JsonResult<List<AccountOperationBean>> listOperations(@RequestBody final AccountOperationStateParam accountOperationStateParam) {
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
			jsonResult.addError(TECHNICAL_EXCEPTION);
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
	@RequestMapping(value = CREDIT_ACCOUNT_REST, method = { RequestMethod.POST }, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('bankmanager')")
	public JsonResult<Object> creditAccount(@RequestBody final OperationUserParam operationUserParam) {

		LOGGER.info("BEGIN of the method creditAccount of the class " + BanqueController.class.getName()
				+ " ( userIdentifier = " + operationUserParam.getIdentifier() + " , labelOperation  = "
				+ operationUserParam.getLabelOperation() + " )");

		JsonResult<Object> jsonResult = new JsonResult<>();

		try {
			banqueService.creditAmount(operationUserParam.getIdentifier(), operationUserParam.getLabelOperation(),
					operationUserParam.getAmount());
			jsonResult.setSuccess(true);
		} catch (UserBanqueNotFoundException | MaxAmountCreditException exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(exception.getMessage());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(TECHNICAL_EXCEPTION);
		}

		LOGGER.info("END of the method creditAccount of the class " + BanqueController.class.getName()
				+ " ( userIdentifier = " + operationUserParam.getIdentifier() + " , labelOperation  = "
				+ operationUserParam.getLabelOperation() + " )");

		return jsonResult;
	}

	@RequestMapping(value = DEBIT_ACCOUNT_REST, method = { RequestMethod.POST }, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('bankmanager')")
	public JsonResult<BigDecimal> debitAccount(@RequestBody final OperationUserParam operationUserParam) {

		LOGGER.info("BEGIN of the method debitAccount of the class " + BanqueController.class.getName()
				+ " ( userIdentifier = " + operationUserParam.getIdentifier() + " , labelOperation  = "
				+ operationUserParam.getLabelOperation() + " )");

		JsonResult<BigDecimal> jsonResult = new JsonResult<>();
		try {
			final BigDecimal balanceAmount = banqueService.debitAmount(operationUserParam.getIdentifier(),
					operationUserParam.getLabelOperation(), operationUserParam.getAmount());
			jsonResult.setResult(balanceAmount);
			jsonResult.setSuccess(true);
		} catch (UserBanqueNotFoundException | NegativeBalanceAmountException exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(exception.getMessage());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(TECHNICAL_EXCEPTION);
		}

		LOGGER.info("END of the method debitAccount of the class " + BanqueController.class.getName()
				+ " ( userIdentifier = " + operationUserParam.getIdentifier() + " , labelOperation  = "
				+ operationUserParam.getLabelOperation() + " )");

		return jsonResult;
	}

	@RequestMapping(value = BALANCE_USER_REST, method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasRole('bankmanager') OR hasRole('bankcollaborator')")
	public JsonResult<BigDecimal> retrieveBalanceAccountUser(
			@RequestParam(name = "userIdentifier", required = true) final String userIdentifier) {

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
			jsonResult.addError(TECHNICAL_EXCEPTION);
		}
		LOGGER.info("END of the method debitAccount of the class " + BanqueController.class.getName() + " ( userIdentifier = " + userIdentifier + " ) ");

		return jsonResult;
	}
}
