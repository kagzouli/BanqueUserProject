package com.exakaconsulting.exabanque.appli;

public interface IConstantExabanque {

	/** Rest template bean **/
	static final String REST_TEMPLATE_BEAN = "RestTemplateBean";
	
	static final String USER_NOT_FOUND_ERROR = "exabanque.usernotfound.error";
	
	static final String MAX_AMOUNT_ERROR = "exabanque.maxamount.error";
	
	/** Key negative balance amount **/
	static final String NEGATIVE_BALANCE_AMOUNT_ERROR = "exabanque.negativebalance.amount";
	
	/** Banque technical error **/
	static final String TECHNICAL_EXCEPTION = "exabanque.technical.error";

	
}
