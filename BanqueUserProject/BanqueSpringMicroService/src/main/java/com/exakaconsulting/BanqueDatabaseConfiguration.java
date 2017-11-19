package com.exakaconsulting;

import static com.exakaconsulting.IConstantApplication.BANQUE_DATASOURCE_BEAN;
import static com.exakaconsulting.IConstantApplication.TRANSACTIONAL_BANQUE_BEAN;
import static com.exakaconsulting.IConstantApplication.MONGO_PROFILE;


import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Profile("!"+ MONGO_PROFILE)
public class BanqueDatabaseConfiguration {
	
	@Bean(BANQUE_DATASOURCE_BEAN)
	@Primary
	public DataSource banqueDatasource() {
		JndiDataSourceLookup jndiBanqueDatasourceLookup = new JndiDataSourceLookup();
		return jndiBanqueDatasourceLookup.getDataSource("java:comp/env/" + BanqueConfig.JNDI_BANQUE_DATASOURCE);
	}

	@Bean(TRANSACTIONAL_BANQUE_BEAN)
	public PlatformTransactionManager transactionBanqueBean(final ApplicationContext appContext) {
		return new DataSourceTransactionManager(appContext.getBean(BANQUE_DATASOURCE_BEAN, DataSource.class));
	}


}
