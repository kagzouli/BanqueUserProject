package com.exakaconsulting;

import java.math.BigDecimal;

public interface IConstantApplication {
	
	/** Banque service **/
	static final String BANQUE_SERVICE = "BanqueService";
	
	/** Max amount autorized **/
	static final BigDecimal MAX_AMOUNT_AUTORIZED = BigDecimal.valueOf(500);
	
	/** Big decimal zero **/
	static final BigDecimal BIG_DECIMAL_ZERO = BigDecimal.ZERO;
	
	
	/** Key max amount exception  **/
	static final String KEY_MAX_AMOUNT_EXCEPTION = "banque.maxamount.authorized";
	
	/** Key negative balance amount **/
	static final String NEGATIVE_BALANCE_AMOUNT_EXCEPTION = "banque.negativebalance.amount";
		
	static final String USER_NOT_FOUND_EXCEPTION = "banque.user.notfound";
		
	/** Banque datasource spring bean **/
	static final String BANQUE_DATASOURCE_BEAN = "BanqueDataSourceBean";

	/** Transactional banque bean **/
	static final String TRANSACTIONAL_BANQUE_BEAN  = "TransactionalBanqueBean";
	
	/** Rest template bean **/
	static final String REST_TEMPLATE_BEAN = "RestTemplateBean";
	
	/** Credit operation type **/
	public static final String CREDIT_OPERATION_TYPE = "CRED";
	
	/** Debit operation type  **/
	public static final String DEBIT_OPERATION_TYPE = "DEBI";
	
	/** REST list operations **/
	public static final String LIST_OPERATION_REST = "/listOperations";
	
	/** REST credit account user **/
	public static final String CREDIT_ACCOUNT_REST = "/creditAccount";
	
	/** REST debit account user  */
	public static final String DEBIT_ACCOUNT_REST = "/debitAccount";

	/** REST balance user  */
	public static final String BALANCE_USER_REST = "/balanceAccountUser";
}
