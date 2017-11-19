package com.exakaconsulting.banque.service;

import static com.exakaconsulting.IConstantApplication.BANQUE_DATASOURCE_BEAN;
import static com.exakaconsulting.IConstantApplication.TRANSACTIONAL_BANQUE_BEAN;
import static com.exakaconsulting.IConstantApplication.REST_TEMPLATE_BEAN;

import javax.sql.DataSource;

import org.h2.Driver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import com.exakaconsulting.websocket.DisplayDateHandler;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.exakaconsulting.banque.*" , "com.exakaconsulting.websocket.*"})
@SpringBootApplication
@EnableEurekaClient
public class BanqueApplicationTest {


	@Bean(BANQUE_DATASOURCE_BEAN)
	@Primary
	public DataSource banqueDatasource(){
		DriverManagerDataSource banqueDatasource = new DriverManagerDataSource();
		banqueDatasource.setDriverClassName(Driver.class.getName());
		banqueDatasource.setUrl("jdbc:h2:~/data/banque1;INIT=create schema if not exists banque1\\;RUNSCRIPT FROM 'classpath:db-data-h2-banque.sql'");
		banqueDatasource.setUsername("sa");
		banqueDatasource.setPassword("");
		return banqueDatasource;
	}
	
	@Bean(TRANSACTIONAL_BANQUE_BEAN)
	public PlatformTransactionManager transactionBanqueBean(final ApplicationContext appContext){
		return new DataSourceTransactionManager(appContext.getBean(BANQUE_DATASOURCE_BEAN, DataSource.class));
	}
	
	@Bean
	@LoadBalanced
	@Qualifier(REST_TEMPLATE_BEAN)
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
	@Bean
	public DisplayDateHandler displayDateHandler(){
		return new DisplayDateHandler();
	}


}
