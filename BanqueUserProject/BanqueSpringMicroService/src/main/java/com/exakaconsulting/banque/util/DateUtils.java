package com.exakaconsulting.banque.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
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
	public static Date fixDate(final int year , final int month , final int day , final int hour , final int minute , final int second , final int millisecond){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH , day);
		calendar.set(Calendar.HOUR_OF_DAY , hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, millisecond);
		return calendar.getTime();

	}


}
