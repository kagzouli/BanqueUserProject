package com.exakaconsulting;

import javax.sql.DataSource;

import static com.exakaconsulting.IConstantUserApplication.*;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.h2.Driver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.exakaconsulting.user.*")
@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
public class UserApplication {

	private static final String JNDI_USER_DATASOURCE = "jndi/userDatasource";

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
	                        banqueDatasource.setName(JNDI_USER_DATASOURCE);
	                        banqueDatasource.setAuth("Container");
	                        banqueDatasource.setType("javax.sql.DataSource");
	                        banqueDatasource.setScope("Sharable");
	                        banqueDatasource.setProperty("driverClassName", Driver.class.getName());
	                        banqueDatasource.setProperty("url", "jdbc:h2:~/data/users1;INIT=create schema if not exists users1\\;RUNSCRIPT FROM 'classpath:db-data-h2-users.sql'");
	                        banqueDatasource.setProperty("username", "sa");
	                        banqueDatasource.setProperty("password", "");

	                        context.getNamingResources().addResource(banqueDatasource);
	                    }
	                });
	            }
	        }
	    };
	}
	
	@Bean(USER_DATASOURCE_BEAN)
	@Primary
	public DataSource banqueDatasource(){
		JndiDataSourceLookup jndiBanqueDatasourceLookup = new JndiDataSourceLookup();
		return jndiBanqueDatasourceLookup.getDataSource("java:comp/env/" + JNDI_USER_DATASOURCE);
	}
	
	/** For swagger-ui **/
	@Bean
	public Docket userApi(){
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.exakaconsulting.user.web"))
				.paths(PathSelectors.regex("/*"))
				.build();
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(UserApplication.class, args);
	}

	
}
