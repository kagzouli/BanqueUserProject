package com.exakaconsulting;

public interface IConstantUserApplication {
	

	/** User service **/
	static final String USER_SERVICE = "UserService";

	
	/** User datasource spring bean **/
	static final String USER_DATASOURCE_BEAN = "UserDataSourceBean";
	
	/** Transactional banque bean **/
	static final String TRANSACTIONAL_USER_BEAN  = "TransactionalUserBean";

	
	/** User not found **/
	static final String USER_NOT_FOUND_EXCEPTION = "user.notfound";

	/** User not found **/
	static final String USER_ALREADY_EXISTS_EXCEPTION = "user.alreadyexists";
	
	
	/** REST users list  */
	static final String USERS_LIST_REST = "/userslist";
	
	/** REST roles list **/
	static final String ROLES_LIST_REST = "/roleslist";
	
	/** REST user list by code **/
	static final String USERS_BYCODE_REST = "/userByCode";
	
	/** REST insert user  */
	static final String INSERT_USER_REST = "/insertUser";

	/** REST insert user  */
	static final String UPDATE_USER_REST = "/updateUser";

	/** REST insert user  */
	static final String DELETE_USER_REST = "/deleteUser";


}
