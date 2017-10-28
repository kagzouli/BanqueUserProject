package com.exakaconsulting.exabanque.web;


import static com.exakaconsulting.exabanque.appli.IConstantExabanque.TECHNICAL_EXCEPTION;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.exakaconsulting.exabanque.exception.ExaMaxAmountCreditException;
import com.exakaconsulting.exabanque.exception.ExaNegativeBalanceAmountException;
import com.exakaconsulting.exabanque.exception.TechnicalException;
import com.exakaconsulting.exabanque.exception.UserExaBanqueNotFoundException;
import com.exakaconsulting.exabanque.service.AccountOperationStateParam;
import com.exakaconsulting.exabanque.service.ExaAccountOperationBean;
import com.exakaconsulting.exabanque.service.ExaUserBean;
import com.exakaconsulting.exabanque.service.IExabanqueService;
import com.exakaconsulting.exabanque.service.IExauserService;
import com.exakaconsulting.exabanque.service.JsonResult;
import com.exakaconsulting.exabanque.service.OperationUserParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "/", description = "This REST API is use to make actions on exabanque.<br/>")
public class ExabanqueController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExabanqueController.class);

	static final String BALANCE_USER_REST = "/userBalance";
	
	static final String USERS_LIST_REST = "/usersList";
	
	/** REST credit account user **/
	public static final String CREDIT_ACCOUNT_REST = "/creditAccount";
	
	/** REST debit account user  */
	public static final String DEBIT_ACCOUNT_REST = "/debitAccount";

	
	/** REST list operations **/
	static final String LIST_OPERATION_REST = "/listOperations";


	@Autowired
	private IExabanqueService exabanqueService;
	
	@Autowired
	private IExauserService exauserService;
	
	@ApiOperation(value = "This method is use to retrieve the home of the exabank", response = ModelAndView.class)
	@RequestMapping(value = "/exabanque", method = {RequestMethod.GET })
	public ModelAndView home() {
		ModelAndView modelView = new ModelAndView("exabanque-jsp/exabanqueHomePage");

		modelView.addObject("TEST", "KARIM");

		return modelView;

	}

	
	@ApiOperation(value = "This method is to retrieve the balance of a user in the exabanque", response = JsonResult.class , notes="JsonResult of BigDecimal")
	@RequestMapping(value = BALANCE_USER_REST, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public  JsonResult<BigDecimal> retrieveBalanceAccountUser(
			@ApiParam(value = "The user identifier to get the balance", required=true) @RequestParam(name = "userIdentifier", required = true) final String userIdentifier) {

		LOGGER.info("BEGIN of the method debitAccount of the class " + ExabanqueController.class.getName() + " ( userIdentifier = " + userIdentifier + " ) ");
		
		JsonResult<BigDecimal> jsonResult = new JsonResult<>();
		try {
			final BigDecimal sumAccountUser = exabanqueService.retrieveBalanceAmount(userIdentifier);
			jsonResult.setResult(sumAccountUser);
			jsonResult.setSuccess(true);
		} catch (UserExaBanqueNotFoundException exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(exception.getMessage());
		}catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(TECHNICAL_EXCEPTION);
		}
		LOGGER.info("END of the method debitAccount of the class " + ExabanqueController.class.getName() + " ( userIdentifier = " + userIdentifier + " ) ");

		return jsonResult;
	}
	

	@ApiOperation(value = "This method is use to get the list of users in the exabanque", response = ExaUserBean.class , responseContainer="List")
	@RequestMapping(value = USERS_LIST_REST, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public List<ExaUserBean> retrieveUsersMap() {

		LOGGER.info("BEGIN of the method retrieveUsersList of the class " + ExabanqueController.class.getName());

		List<ExaUserBean> usersList = Collections.<ExaUserBean>emptyList();
		try {
			usersList = exauserService.retrieveUsersList();
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method retrieveUsersList of the class " + ExabanqueController.class.getName());

		return usersList;
	}
	

	@ApiOperation(value = "This method is use to get the list of account operations of a user in the exabanque using some inputs", response = ExaAccountOperationBean.class , responseContainer="List")
	@RequestMapping(value = LIST_OPERATION_REST, method = { RequestMethod.GET }, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public List<ExaAccountOperationBean> listOperations(@ApiParam(value="The account operations to search") @RequestBody(required=true) final AccountOperationStateParam accountOperationStateParam) throws UserExaBanqueNotFoundException  {
		
		accountOperationStateParam.setBeginDate(this.changeHourMinSecDate(accountOperationStateParam.getBeginDate(), 0, 0, 0));
		accountOperationStateParam.setEndDate(this.changeHourMinSecDate(accountOperationStateParam.getEndDate(), 23, 59, 59));
		
		LOGGER.info("BEGIN of the method listOperations of the class " + ExabanqueController.class.getName()
				+ "identifierUser : " + accountOperationStateParam.getUserIdentifier() + " , Begin date : "
				+ accountOperationStateParam.getBeginDate() + " , End Date : "
				+ accountOperationStateParam.getEndDate());		
		List<ExaAccountOperationBean> listOperationAccount = Collections.<ExaAccountOperationBean>emptyList();
		try {			
			
			if (!StringUtils.isBlank(accountOperationStateParam.getUserIdentifier())){
				listOperationAccount = exabanqueService.retrieveListOperations(accountOperationStateParam);
			}
			
		}catch(UserExaBanqueNotFoundException exception){
			throw exception;
		}
		catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		LOGGER.info("END of the method listOperations of the class " + ExabanqueController.class.getName()
				+ "identifierUser : " + accountOperationStateParam.getUserIdentifier() + " , Begin date : "
				+ accountOperationStateParam.getBeginDate() + " , End Date : "
				+ accountOperationStateParam.getEndDate());

		return listOperationAccount;
	}
	
	
	/**
	 * credit Account for user.<br/>
	 * 
	 */
	@ApiOperation(value = "This method is to credit a amount to an user account", response = JsonResult.class)
	@RequestMapping(value = CREDIT_ACCOUNT_REST, method = { RequestMethod.POST }, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public JsonResult<Object> creditAccount(@ApiParam(value="The account operation to credit for the user.", required=true) @RequestBody(required=true) final OperationUserParam operationUserParam) {

		LOGGER.info("BEGIN of the method creditAccount of the class " + ExabanqueController.class.getName()
				+ " ( userIdentifier = " + operationUserParam.getIdentifier() + " , labelOperation  = "
				+ operationUserParam.getLabelOperation() + " )");

		JsonResult<Object> jsonResult = new JsonResult<>();

		try {
			exabanqueService.creditAmount(operationUserParam);
			jsonResult.setSuccess(true);
		} catch (ExaMaxAmountCreditException exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(exception.getMessage());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(TECHNICAL_EXCEPTION);
		}

		LOGGER.info("END of the method creditAccount of the class " + ExabanqueController.class.getName()
				+ " ( userIdentifier = " + operationUserParam.getIdentifier() + " , labelOperation  = "
				+ operationUserParam.getLabelOperation() + " )");

		return jsonResult;
	}

	@ApiOperation(value = "This method is to debit a amount to an user account", response = JsonResult.class)
	@RequestMapping(value = DEBIT_ACCOUNT_REST, method = { RequestMethod.POST }, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public JsonResult<BigDecimal> debitAccount(@ApiParam(value="The account operation to credit for the user.", required=true) @RequestBody(required=true) final OperationUserParam operationUserParam) {

		LOGGER.info("BEGIN of the method debitAccount of the class " + ExabanqueController.class.getName()
				+ " ( userIdentifier = " + operationUserParam.getIdentifier() + " , labelOperation  = "
				+ operationUserParam.getLabelOperation() + " )");

		JsonResult<BigDecimal> jsonResult = new JsonResult<>();
		try {
			final BigDecimal balanceAmount = exabanqueService.debitAmount(operationUserParam);
			jsonResult.setResult(balanceAmount);
			jsonResult.setSuccess(true);
		} catch (ExaNegativeBalanceAmountException exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(exception.getMessage());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			jsonResult.addError(TECHNICAL_EXCEPTION);
		}

		LOGGER.info("END of the method debitAccount of the class " + ExabanqueController.class.getName()
				+ " ( userIdentifier = " + operationUserParam.getIdentifier() + " , labelOperation  = "
				+ operationUserParam.getLabelOperation() + " )");

		return jsonResult;
	}
	
	
	/**
	 * 
	 * @param date
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	private Date changeHourMinSecDate(final Date date, final int hour , final int minute, final int second){
		Date dateFinal = null;
		if (date != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE , minute);
			calendar.set(Calendar.SECOND , second);
			dateFinal = calendar.getTime();
		}
		return dateFinal;
	}

	
	@ExceptionHandler({UserExaBanqueNotFoundException.class})
	  public String userNotFoundError(UserExaBanqueNotFoundException exception) {
	    // Nothing to do.  Returns the logical view name of an error page, passed
	    // to the view-resolver(s) in usual way.
	    // Note that the exception is NOT available to this view (it is not added
	    // to the model) but see "Extending ExceptionHandlerExceptionResolver"
	    // below
	    return exception.getMessage();
	  }


	
}
