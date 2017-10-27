package com.exakaconsulting.banque.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.exakaconsulting.banque.service.AccountOperationBean;

public class AccountOperationRowMapper implements RowMapper<AccountOperationBean>{

	@Override
	public AccountOperationBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		AccountOperationBean accountOperationBean = new AccountOperationBean(); 
		
		accountOperationBean.setOperationType(rs.getString("operationType"));
		
		accountOperationBean.setLabel(rs.getString("labelOperation"));
		
		Timestamp timeOperationDate = rs.getTimestamp("operationDate");
		if (timeOperationDate != null){
			accountOperationBean.setOperationDate(new Date(timeOperationDate.getTime()));			
		}
		accountOperationBean.setUserIdentifier(rs.getString("identifierUser"));
		accountOperationBean.setAmount(rs.getBigDecimal("amount"));		
		return accountOperationBean;
	}

}
