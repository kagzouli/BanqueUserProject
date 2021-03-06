package com.exakaconsulting;

import static com.exakaconsulting.IConstantApplication.REST_TEMPLATE_BEAN;


import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan({ "com.exakaconsulting.banque.*", "com.exakaconsulting.websocket.*" })
@EnableEurekaClient
@EnableSwagger2
public class BanqueConfig{
	public static final String JNDI_BANQUE_DATASOURCE = "jndi/banqueDatasource";

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
				banqueDatasource.setProperty("url",
						"jdbc:h2:~/data/banque1;INIT=create schema if not exists banque1\\;RUNSCRIPT FROM 'classpath:db-data-h2-banque.sql'");
				banqueDatasource.setProperty("username", "sa");
				banqueDatasource.setProperty("password", "");

				context.getNamingResources().addResource(banqueDatasource);

			}

		};

	}
	
	@Bean
	@LoadBalanced
	@Qualifier(REST_TEMPLATE_BEAN)
	public RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory httpComponentFactory = new HttpComponentsClientHttpRequestFactory();
		return new RestTemplate(httpComponentFactory);
	}

	/** For swagger-ui **/
	@Bean
	public Docket banqueSwaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.exakaconsulting.banque.web"))
				/* .paths(PathSelectors.regex("/*")) */
				.build();
	}

}
