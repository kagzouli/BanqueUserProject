package com.exakaconsulting.user.service;

import static com.exakaconsulting.IConstantUserApplication.USER_DATASOURCE_BEAN;
import static com.exakaconsulting.IConstantUserApplication.TRANSACTIONAL_USER_BEAN;

import javax.sql.DataSource;

import org.h2.Driver;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.HystrixAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.exakaconsulting.user.*")
@SpringBootApplication(exclude={HystrixAutoConfiguration.class})
public class UserApplicationTest {


	@Bean(USER_DATASOURCE_BEAN)
	@Primary
	public DataSource userDatasource(){
		DriverManagerDataSource banqueDatasource = new DriverManagerDataSource();
		banqueDatasource.setDriverClassName(Driver.class.getName());
		banqueDatasource.setUrl("jdbc:h2:~/data/users1;INIT=create schema if not exists users1\\;RUNSCRIPT FROM 'classpath:db-data-h2-users.sql'");
		banqueDatasource.setUsername("sa");
		banqueDatasource.setPassword("");
		return banqueDatasource;
	}
	
	@Bean(TRANSACTIONAL_USER_BEAN)
	public PlatformTransactionManager transactionBanqueBean(final ApplicationContext appContext){
		return new DataSourceTransactionManager(appContext.getBean(USER_DATASOURCE_BEAN, DataSource.class));
	}

}
