package com.exakaconsulting.banque.dao;

import static com.exakaconsulting.IConstantApplication.REST_TEMPLATE_BEAN;
import static com.exakaconsulting.IConstantApplication.USER_NOT_FOUND_EXCEPTION;

import java.nio.charset.Charset;

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
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.exakaconsulting.banque.service.BanqueUserBean;
import com.exakaconsulting.banque.service.UserBanqueNotFoundException;

@Repository
public class BanqueUserDaoImpl implements IBanqueUserDao {

	private static final String URL_USER_BY_CODE = "http://user-service/userByCode";

	private static final String USERNAME = "banque";

	private static final String PASSWORD = "baNqUe35#";

	@Autowired
	@Qualifier(REST_TEMPLATE_BEAN)
	@LoadBalanced
	private RestTemplate restTemplate;

	@Override
	public BanqueUserBean retrieveUserByIdentifier(String userIdentifier) throws UserBanqueNotFoundException {

		Assert.hasLength(userIdentifier, "userIdentifier must be set");

		// Get the user.
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("userCode", userIdentifier);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", this.stringSecurity());
		// set your entity to send
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

		BanqueUserBean user = null;
		try {
			final ResponseEntity<JsonBanqueUserResult> entityJsonResult = restTemplate.exchange(URL_USER_BY_CODE,
					HttpMethod.GET, entity, JsonBanqueUserResult.class);

			if (entityJsonResult == null || entityJsonResult.getBody() == null
					|| !entityJsonResult.getBody().isSuccess()) {
				throw new UserBanqueNotFoundException(USER_NOT_FOUND_EXCEPTION);
			}

			user = (BanqueUserBean) entityJsonResult.getBody().getResult();
		} catch (HttpClientErrorException clientErrorException) {
			if (HttpStatus.UNAUTHORIZED.equals(clientErrorException.getStatusCode())) {
				throw new UserBanqueNotFoundException(USER_NOT_FOUND_EXCEPTION);
			}else if (HttpStatus.FORBIDDEN.equals(clientErrorException.getStatusCode())) {
				throw new UserBanqueNotFoundException(USER_NOT_FOUND_EXCEPTION);
			}			
		}
		return user;

	}

	/**
	 * Method to have the string security.
	 * 
	 * @return
	 */
	private String stringSecurity() {
		String auth = USERNAME + ":" + PASSWORD;
		byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		return authHeader;
	}

}
