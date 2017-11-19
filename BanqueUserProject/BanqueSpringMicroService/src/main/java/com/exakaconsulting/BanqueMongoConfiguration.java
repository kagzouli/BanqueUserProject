package com.exakaconsulting;

import static com.exakaconsulting.IConstantApplication.MONGO_PROFILE;
import static com.exakaconsulting.IConstantApplication.TRANSACTIONAL_BANQUE_BEAN;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.integration.transaction.PseudoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.exakaconsulting.mongo.converter.BigDecimalToDoubleConverter;
import com.exakaconsulting.mongo.converter.DoubleToBigDecimalConverter;
import com.mongodb.MongoClient;
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
        MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
        mongoMapping.setCustomConversions(this.customConversions()); // tell mongodb to use the custom converters
        mongoMapping.afterPropertiesSet();
        return mongoTemplate;
    }
    
    /**
     * Returns the list of custom converters that will be used by the MongoDB template
     * 
     **/
     public CustomConversions customConversions() {
         return new CustomConversions(CustomConversions.StoreConversions.NONE,Arrays.asList(new DoubleToBigDecimalConverter(), new BigDecimalToDoubleConverter()));
     }
    
	@Bean(TRANSACTIONAL_BANQUE_BEAN)
	public PlatformTransactionManager transactionBanqueBean(final ApplicationContext appContext) {
		return new PseudoTransactionManager();
	}


}
