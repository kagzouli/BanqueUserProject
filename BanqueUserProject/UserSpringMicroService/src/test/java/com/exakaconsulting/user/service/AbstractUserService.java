package com.exakaconsulting.user.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * Class abstract for the test service.<br/>
 * 
 * @author Exaka consulting.<br/>
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=UserApplicationTest.class)
@Transactional
public class AbstractUserService {
	
	protected static final String IDENTIFIER_USER_TEST = "ROGFED"; 
	
	protected static final String IDENTIFIER_USER_NOTEXISTS = "USERNOTEXISTS";
	
	protected static final String IDENTIFIER_NEW_USER = "NOVDJO";
	
	protected static final String FIRST_NAME_NEW_USER = "Novak";
	
	protected static final String LAST_NAME_NEW_USER = "Djokovic";
	
	protected static final String LOCALE_NEW_USER = "EN";
	
	protected static final String FIRST_NAME_USER_TEST = "Roger";
	
	protected static final String LAST_NAME_USER_TEST  = "Federer";
	
	protected static final String ROLE_TEST_USER = "role1";
	
	


}
