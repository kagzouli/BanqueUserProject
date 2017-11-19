package com.exakaconsulting;

import java.util.Arrays;

import javax.sql.DataSource;

import static com.exakaconsulting.IConstantApplication.BANQUE_DATASOURCE_BEAN;
import static com.exakaconsulting.IConstantApplication.MONGO_PROFILE;
import static com.exakaconsulting.IConstantApplication.TRANSACTIONAL_BANQUE_BEAN;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.integration.transaction.PseudoTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@Profile(MONGO_PROFILE)
public class BanqueMongoConfiguration {
	
	private static final String DATABASE_NAME = "banqueuserdb";
	
	@Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
		
		ServerAddress serverAddress = new ServerAddress("localhost", 27017);
		
		//MongoCredential userCredentials = MongoCredential.createCredential("", DATABASE_NAME,new char[]{});
        
       // MongoClient mongoClient = new MongoClient(serverAddress, Arrays.asList(userCredentials));
	    MongoClient mongoClient = new MongoClient(serverAddress, Arrays.asList());
		return new SimpleMongoDbFactory(mongoClient, DATABASE_NAME);
    }
	
    @Bean
    public MongoTemplate mongoTemplate(ApplicationContext context) throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(context.getBean(MongoDbFactory.class));
        return mongoTemplate;
    }
    
	@Bean(TRANSACTIONAL_BANQUE_BEAN)
	public PlatformTransactionManager transactionBanqueBean(final ApplicationContext appContext) {
		return new PseudoTransactionManager();
	}


}
