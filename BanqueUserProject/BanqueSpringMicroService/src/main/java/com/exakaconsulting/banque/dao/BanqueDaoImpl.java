package com.exakaconsulting.banque.dao;

import static com.exakaconsulting.IConstantApplication.BIG_DECIMAL_ZERO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.exakaconsulting.IConstantApplication;
import com.exakaconsulting.banque.service.AccountOperationBean;
import com.exakaconsulting.exception.TechnicalException;

@Repository
public class BanqueDaoImpl implements IBanqueDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(BanqueDaoImpl.class);

	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String BEGIN_ACCOUNT_OPERATION_SQL = "select * from OPERATION_USER_TABLE ";

	private static final String INSERT_ACCOUNT_OPERATION_SQL = "INSERT INTO OPERATION_USER_TABLE(operationType,identifierUser,labelOperation,operationDate,amount) values (:operationTypeParam,:identifierUserParam ,:labelOperationParam,:operationDateParam , :amountParam)";

	private static final String SUM_AMOUNT_ACCOUNT_OPTYPEUSER = "SELECT sum(amount) as sumAmount from OPERATION_USER_TABLE WHERE identifierUser = :identifierUserParam and operationType = :operationTypeParam";

	@Autowired
	@Qualifier(IConstantApplication.BANQUE_DATASOURCE_BEAN)
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public void insertAccountOperation(final AccountOperationBean accountOperationBean) {
		Assert.notNull(accountOperationBean, "The accountOperationBean must be set");

		try {
			Map<String, Object> params = new HashMap<>();
			params.put("operationTypeParam", accountOperationBean.getOperationType());
			params.put("identifierUserParam", accountOperationBean.getUserIdentifier());
			params.put("labelOperationParam", accountOperationBean.getLabel());
			params.put("operationDateParam", accountOperationBean.getOperationDate());
			params.put("amountParam", accountOperationBean.getAmount());

			int returnValue = this.jdbcTemplate.update(INSERT_ACCOUNT_OPERATION_SQL, params);

			if (returnValue <= 0) {
				throw new TechnicalException("No insert has been done for accountOperation");
			}
		} catch (TechnicalException exception) {
			throw exception;
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception.getMessage());
		}

	}

	@Override
	public List<AccountOperationBean> retrieveOperations(String identifierUser, Date beginDate, Date endDate) {

		Assert.hasLength(identifierUser, "The identifier must be set");

		LOGGER.info("BEGIN of the method retrieveOperations of the class " + BanqueDaoImpl.class.getName());

		List<AccountOperationBean> listAccountOperation = Collections.<AccountOperationBean>emptyList();
		try {
			Map<String, Object> params = new HashMap<>();

			List<String> listWhereVariable = new ArrayList<>();
			listWhereVariable.add("identifierUser = :identifierUserParam");
			params.put("identifierUserParam", identifierUser);

			if (beginDate != null) {
				listWhereVariable.add("operationDate >= :beginDateParam");
				params.put("beginDateParam", beginDate);
			}

			if (endDate != null) {
				listWhereVariable.add("operationDate <= :endDateParam");
				params.put("endDateParam", endDate);
			}

			StringBuilder requestSql = new StringBuilder(64);
			requestSql.append(BEGIN_ACCOUNT_OPERATION_SQL);
			if (listWhereVariable != null && !listWhereVariable.isEmpty()) {
				requestSql.append(" WHERE ");
				requestSql.append(StringUtils.join(listWhereVariable, " AND "));
				requestSql.append(" ORDER BY operationDate DESC");
			}

			listAccountOperation = jdbcTemplate.query(requestSql.toString(), params, new AccountOperationRowMapper());
			LOGGER.info("END of the method retrieveOperations of the class " + BanqueDaoImpl.class.getName());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}

		return listAccountOperation;
	}

	@Override
	public BigDecimal retrieveSumAmountOperTypeByIdentifierUser(final String identifierUser,
			final String operationType) {
		Assert.hasLength(identifierUser, "Identifier user must be set");
		Assert.hasLength(operationType, "Operation type must be set");

		Map<String, Object> params = new HashMap<>();
		params.put("identifierUserParam", identifierUser);
		params.put("operationTypeParam", operationType);

		BigDecimal sumAccount = BIG_DECIMAL_ZERO;
		try {
			sumAccount = jdbcTemplate.queryForObject(SUM_AMOUNT_ACCOUNT_OPTYPEUSER, params, BigDecimal.class);
		} catch (EmptyResultDataAccessException exception) {
			LOGGER.warn("No result for the requestretrieveSumAmountOperTypeByIdentifierUser with parameter " + params);
			sumAccount = BIG_DECIMAL_ZERO;
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new TechnicalException(exception);
		}
		return sumAccount;
	}
}
