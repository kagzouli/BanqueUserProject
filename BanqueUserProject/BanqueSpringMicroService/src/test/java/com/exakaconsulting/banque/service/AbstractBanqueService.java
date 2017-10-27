package com.exakaconsulting.banque.service;

import java.util.Date;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.exakaconsulting.banque.util.DateUtils;

/**
 * 
 * Class abstract for the test service.<br/>
 * 
 * @author Exaka consulting.<br/>
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=BanqueApplicationTest.class)
@Transactional
public class AbstractBanqueService {
	
	protected static final String IDENTIFIER_USER_TEST = "ROGFED"; 
	
	/**
	 * Method retrieve the data with the informations
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minutes
	 * @param second
	 * @param millisecond
	 * @return
	 */
	protected Date fixDate(final int year , final int month , final int day , final int hour , final int minute , final int second , final int millisecond){
		return DateUtils.fixDate(year , month , day , hour , minute, second , millisecond);
	}

}
