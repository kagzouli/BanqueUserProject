package com.exakaconsulting.exabanque.service;

import static com.exakaconsulting.exabanque.appli.IConstantExabanque.REST_TEMPLATE_BEAN;

import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.exakaconsulting.exabanque.exception.TechnicalException;

@Service
public class ExauserServiceImpl implements IExauserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExauserServiceImpl.class);

	private static final String BEGIN_USER_SERVICE = "http://user-service/";

	private static final String USER_LIST_REST = BEGIN_USER_SERVICE + "/userslist";


	private static final String USERNAME = "banque";

	private static final String PASSWORD = "baNqUe35#";
	
	@Autowired
	@Qualifier(REST_TEMPLATE_BEAN)
	@LoadBalanced
	private RestTemplate restTemplate;
	


	@Override
	public List<ExaUserBean> retrieveUsersList() {

		LOGGER.info("BEGIN of the method retrieveUsersList of the class " + ExauserServiceImpl.class.getName());


		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", this.stringSecurity());
		// set your entity to send
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

		
		final ResponseEntity<JsonResultListExakaUser> entityJsonResult = restTemplate.exchange(USER_LIST_REST,
				HttpMethod.POST , entity, JsonResultListExakaUser.class);

		if (entityJsonResult == null || entityJsonResult.getBody() == null || !entityJsonResult.getBody().isSuccess()) {
			throw new TechnicalException("technical error");
		}

		List<ExaUserBean> listUsers = (List<ExaUserBean>) entityJsonResult.getBody().getResult();

		LOGGER.info("END of the method retrieveUsersList of the class " + ExauserServiceImpl.class.getName());
		return listUsers;
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
