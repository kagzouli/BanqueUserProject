package com.exakaconsulting;

import static com.exakaconsulting.IConstantApplication.BANQUE_DATASOURCE_BEAN;
import static com.exakaconsulting.IConstantApplication.REST_TEMPLATE_BEAN;
import static com.exakaconsulting.IConstantApplication.TRANSACTIONAL_BANQUE_BEAN;

import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
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

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.exakaconsulting.banque.*")
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
public class BanqueApplication {
	
	private static final String JNDI_BANQUE_DATASOURCE = "jndi/banqueDatasource";

	@Bean
	public TomcatServletWebServerFactory tomcatFactory() {
	    return new TomcatServletWebServerFactory() {	    	
	    	protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
	    		tomcat.enableNaming();
	    		return super.getTomcatWebServer(tomcat);
	    	}
	        
		    @Override
		    protected void configureContext(Context context, ServletContextInitializer[] initializers) {
		    	super.configureContext(context, initializers);
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
	
	/** For swagger-ui **/
	@Bean
	public Docket banqueSwaggerApi(){
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.exakaconsulting.banque.web"))
				/*.paths(PathSelectors.regex("/*"))*/
				.build();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BanqueApplication.class, args);
	}

}
