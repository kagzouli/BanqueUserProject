package com.exakaconsulting.banque.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.exakaconsulting.IConstantApplication.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.previousOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.exakaconsulting.banque.service.AccountOperationBean;

@Repository
@Profile(MONGO_PROFILE)
public class BanqueMongoDaoImpl implements IBanqueDao{
		
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public void insertAccountOperation(AccountOperationBean accountOperationBean) {
		//En cas d'update
		//this.mongoTemplate.save(accountOperationBean);
		
		this.mongoTemplate.insert(accountOperationBean);
		
	}

	@Override
	public List<AccountOperationBean> retrieveOperations(String userIdentifier, Date beginDate, Date endDate) {
		
		Criteria criteria = Criteria.where("userIdentifier").is(userIdentifier);

		List<Criteria> listCriteriaDate = new ArrayList<>();

		// Test on begin date
		if (beginDate != null){
			listCriteriaDate.add(Criteria.where("operationDate").gte(beginDate));
		}
		
		// Test on end date
		if (endDate != null){
			listCriteriaDate.add(Criteria.where("operationDate").lte(endDate));
		}
		
		// Criteria for begin and end date
		if (listCriteriaDate != null && !listCriteriaDate.isEmpty()){
			criteria.andOperator(listCriteriaDate.toArray(new Criteria[listCriteriaDate.size()]));			
		}
		
		Query query = new Query(criteria);
		
		return mongoTemplate.find(query, AccountOperationBean.class, COLLECTIONS_OPERATION_ACCOUNT);
		
	}

	@Override
	public BigDecimal retrieveSumAmountOperTypeByIdentifierUser(String userIdentifier, String operationType) {
		
		Criteria criteria = Criteria.where("userIdentifier").is(userIdentifier).andOperator(Criteria.where("operationType").is(operationType));
		Aggregation aggregation = newAggregation(match(criteria), group("userIdentifier", "operationType").sum("amount").as("total"),
				sort(Sort.Direction.DESC,previousOperation(),"total"));


		AggregationResults<OperAccountRepCount> groupResults = mongoTemplate.aggregate(
			    aggregation, AccountOperationBean.class, OperAccountRepCount.class);
		
		List<OperAccountRepCount> listOperAccountRepCount = groupResults.getMappedResults();

		
		BigDecimal total = BigDecimal.ZERO;
		
		if (listOperAccountRepCount != null && listOperAccountRepCount.size() >= 1){
			total = listOperAccountRepCount.get(0).getTotal();
			total = total.setScale(2, BigDecimal.ROUND_UP);
		}
		
		return total;
	}

}
