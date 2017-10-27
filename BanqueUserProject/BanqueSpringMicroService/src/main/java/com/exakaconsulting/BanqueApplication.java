package com.exakaconsulting;

import javax.sql.DataSource;

import static com.exakaconsulting.IConstantApplication.*; 


import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.exakaconsulting.banque.*")
@SpringBootApplication
@EnableEurekaClient
public class BanqueApplication {
	
	private static final String JNDI_BANQUE_DATASOURCE = "jndi/banqueDatasource";

	@Bean
	public TomcatEmbeddedServletContainerFactory tomcatFactory() {
	    return new TomcatEmbeddedServletContainerFactory() {
	        @Override
	        protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
	                Tomcat tomcat) {
	            tomcat.enableNaming();
	            return super.getTomcatEmbeddedServletContainer(tomcat);
	        }
	    };
	}

	@Bean
	public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
	    return new EmbeddedServletContainerCustomizer() {
	        @Override
	        public void customize(ConfigurableEmbeddedServletContainer container) {
	            if (container instanceof TomcatEmbeddedServletContainerFactory) {
	                TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory = (TomcatEmbeddedServletContainerFactory) container;
	                tomcatEmbeddedServletContainerFactory.addContextCustomizers(new TomcatContextCustomizer() {
	                    @Override
	                    public void customize(Context context) {
	                        ContextResource banqueDatasource = new ContextResource();
	                        banqueDatasource.setName(JNDI_BANQUE_DATASOURCE);
	                        banqueDatasource.setAuth("Container");
	                        banqueDatasource.setType("javax.sql.DataSource");
	                        banqueDatasource.setScope("Sharable");
	                        banqueDatasource.setProperty("driverClassName", "org.h2.Driver");
	                        banqueDatasource.setProperty("url", "jdbc:h2:~/data/banque1;INIT=create schema if not exists banque1\\;RUNSCRIPT FROM 'classpath:db-data-h2-banque.sql'");
	                        banqueDatasource.setProperty("username", "sa");
	                        banqueDatasource.setProperty("password", "");

	                        context.getNamingResources().addResource(banqueDatasource);
	                    }
	                });
	            }
	        }
	    };
	}
	
	@Bean(BANQUE_DATASOURCE_BEAN)
	@Primary
	public DataSource banqueDatasource(){
		JndiDataSourceLookup jndiBanqueDatasourceLookup = new JndiDataSourceLookup();
		return jndiBanqueDatasourceLookup.getDataSource("java:comp/env/" + JNDI_BANQUE_DATASOURCE);
	}
	
	@Bean(TRANSACTIONAL_BANQUE_BEAN)
	public PlatformTransactionManager transactionBanqueBean(final ApplicationContext appContext){
		return new DataSourceTransactionManager(appContext.getBean(BANQUE_DATASOURCE_BEAN, DataSource.class));
	}
	
	@Bean
	@LoadBalanced
	@Qualifier(REST_TEMPLATE_BEAN)
	public RestTemplate restTemplate(){
		HttpComponentsClientHttpRequestFactory httpComponentFactory = new HttpComponentsClientHttpRequestFactory();
		return new RestTemplate(httpComponentFactory);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BanqueApplication.class, args);
	}

}
