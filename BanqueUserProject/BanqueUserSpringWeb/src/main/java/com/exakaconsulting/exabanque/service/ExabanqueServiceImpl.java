package com.exakaconsulting.exabanque.service;

import static com.exakaconsulting.exabanque.appli.IConstantExabanque.USER_NOT_FOUND_ERROR;
import static com.exakaconsulting.exabanque.appli.IConstantExabanque.NEGATIVE_BALANCE_AMOUNT_ERROR;
import static com.exakaconsulting.exabanque.appli.IConstantExabanque.MAX_AMOUNT_ERROR;
import static com.exakaconsulting.exabanque.appli.IConstantExabanque.REST_TEMPLATE_BEAN;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.exakaconsulting.exabanque.exception.ExaMaxAmountCreditException;
import com.exakaconsulting.exabanque.exception.ExaNegativeBalanceAmountException;
import com.exakaconsulting.exabanque.exception.TechnicalException;
import com.exakaconsulting.exabanque.exception.UserExaBanqueNotFoundException;

@Service
public class ExabanqueServiceImpl implements IExabanqueService {

	private static final String BEGIN_BANQUE_SERVICE = "http://banque-service/";

	private static final String RETRIEVE_BALANCE_REST = BEGIN_BANQUE_SERVICE + "/balanceAccountUser";

	/** REST list operations **/
	public static final String LIST_OPERATION_REST = BEGIN_BANQUE_SERVICE + "/listOperations";

	/** REST credit account user **/
	public static final String CREDIT_ACCOUNT_REST = BEGIN_BANQUE_SERVICE + "/creditAccount";

	/** REST debit account user */
	public static final String DEBIT_ACCOUNT_REST = BEGIN_BANQUE_SERVICE + "/debitAccount";

	private static final String BANK_USER_MANAGER = "manager";
	private static final String BANK_PASSWORD_MANAGER = "baNkmaNager35#";

	private static final String BANK_USER_COLLABORATOR = "collaborator";
	private static final String BANK_PASSWORD_COLLABORATOR = "collaBoRator35#";

	@Autowired
	@Qualifier(REST_TEMPLATE_BEAN)
	@LoadBalanced
	private RestTemplate restTemplate;

	@Override
	public BigDecimal retrieveBalanceAmount(String userIdentifier) throws UserExaBanqueNotFoundException {
		Assert.hasLength(userIdentifier, "userIdentifier must be set");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", this.stringSecurityCollaborator());
		// set your entity to send
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

		BigDecimal balance = null;
		try {
			UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(RETRIEVE_BALANCE_REST)
			        .queryParam("userIdentifier", userIdentifier);
			
			final ResponseEntity<JsonResultAmount> entityJsonResult = restTemplate.exchange(urlBuilder.build().encode().toUri(),
					HttpMethod.GET, entity, JsonResultAmount.class);
			
			if (entityJsonResult == null || entityJsonResult.getBody() == null){
				throw new TechnicalException("response Json null");		
			}
			
			this.testUserNotExists(entityJsonResult.getBody());

			balance = entityJsonResult.getBody().getResult();

		} catch (HttpClientErrorException clientErrorException) {
			if (HttpStatus.UNAUTHORIZED.equals(clientErrorException.getStatusCode())) {
				throw new UserExaBanqueNotFoundException(USER_NOT_FOUND_ERROR);
			} else if (HttpStatus.FORBIDDEN.equals(clientErrorException.getStatusCode())) {
				throw new UserExaBanqueNotFoundException(USER_NOT_FOUND_ERROR);
			}else{
				throw new TechnicalException(clientErrorException);
			}

		}


		return balance;

	}

	@Override
	public List<ExaAccountOperationBean> retrieveListOperations(final AccountOperationStateParam accountOperation)
			throws UserExaBanqueNotFoundException {
		Assert.notNull(accountOperation, "The account operation must be set");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", this.stringSecurityCollaborator());
		// set your entity to send
		HttpEntity<AccountOperationStateParam> entity = new HttpEntity<>(accountOperation, headers);
		// send it!
		ResponseEntity<JsonResultListExaAccountOperation> responseJson = restTemplate.exchange(LIST_OPERATION_REST,
				HttpMethod.POST, entity, JsonResultListExaAccountOperation.class);

		if (responseJson == null || responseJson.getBody() == null) {
			throw new TechnicalException("response Json null");
		}

		final JsonResultListExaAccountOperation jsonResult = responseJson.getBody();

		this.testUserNotExists(jsonResult);

		return jsonResult != null ? jsonResult.getResult() : null;
	}

	@Override
	public void creditAmount(OperationUserParam operationUserParam)
			throws ExaMaxAmountCreditException, UserExaBanqueNotFoundException {

		// Call the micro service credit of banque (bank)
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", this.stringSecurityManager());
		// set your entity to send
		HttpEntity<OperationUserParam> entity = new HttpEntity<>(operationUserParam, headers);
		// send it!
		ResponseEntity<JsonResultAmount> responseJson = restTemplate.exchange(CREDIT_ACCOUNT_REST, HttpMethod.POST,
				entity, JsonResultAmount.class);

		if (responseJson == null || responseJson.getBody() == null) {
			throw new TechnicalException("response Json null");
		}

		final JsonResultAmount jsonResult = responseJson.getBody();

		// Test user
		this.testUserNotExists(jsonResult);

		// test max amount reached
		if (jsonResult != null && !jsonResult.isSuccess()) {
			if (jsonResult.getErrors() != null && jsonResult.getErrors().size() > 0) {
				final String errorCode = (String) jsonResult.getErrors().get(0);
				if ("banque.maxamount.authorized".equals(errorCode)) {
					throw new ExaMaxAmountCreditException(MAX_AMOUNT_ERROR);
				}

				throw new TechnicalException("technical error");
			}
		}

	}

	@Override
	public BigDecimal debitAmount(OperationUserParam operationUserParam)
			throws ExaNegativeBalanceAmountException, UserExaBanqueNotFoundException {

		// Call the micro service credit of banque (bank)
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", this.stringSecurityManager());

		// set your entity to send
		HttpEntity<OperationUserParam> entity = new HttpEntity<>(operationUserParam, headers);
		// send it!
		ResponseEntity<JsonResultAmount> responseJson = restTemplate.exchange(DEBIT_ACCOUNT_REST, HttpMethod.POST,
				entity, JsonResultAmount.class);

		if (responseJson == null  || responseJson.getBody() == null) {
			throw new TechnicalException("response Json null");
		}

		final JsonResultAmount jsonResult = responseJson.getBody();

		// Test user
		this.testUserNotExists(jsonResult);

		// test negative balance
		if (jsonResult != null && !jsonResult.isSuccess()) {
			if (jsonResult.getErrors() != null && jsonResult.getErrors().size() > 0) {
				final String errorCode = (String) jsonResult.getErrors().get(0);
				if ("banque.negativebalance.amount".equals(errorCode)) {
					throw new ExaNegativeBalanceAmountException(NEGATIVE_BALANCE_AMOUNT_ERROR);
				}

				throw new TechnicalException("technical error");
			}
		}

		return jsonResult != null ? jsonResult.getResult() : null;
	}

	private void testUserNotExists(final JsonResult jsonResult) throws UserExaBanqueNotFoundException {
		if (jsonResult != null && !jsonResult.isSuccess() && CollectionUtils.isNotEmpty(jsonResult.getErrors())) {
				final String errorCode = (String) jsonResult.getErrors().get(0);
				if ("banque.user.notfound".equals(errorCode)) {
					throw new UserExaBanqueNotFoundException(USER_NOT_FOUND_ERROR);
				}

				throw new TechnicalException("technical error");
			}

	}

	/**
	 * Method to have the string security manager.
	 * 
	 * @return
	 */
	private String stringSecurityManager() {
		String auth = BANK_USER_MANAGER + ":" + BANK_PASSWORD_MANAGER;
		byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		return authHeader;
	}

	/**
	 * Method to have the string security collaborator.<br/>
	 * 
	 * @return
	 */
	private String stringSecurityCollaborator() {
		String auth = BANK_USER_COLLABORATOR + ":" + BANK_PASSWORD_COLLABORATOR;
		byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		return authHeader;
	}

}
