package com.exakaconsulting.exabanque.appli;

import static com.exakaconsulting.exabanque.appli.IConstantExabanque.REST_TEMPLATE_BEAN;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.exakaconsulting.exabanque.*")
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
public class ExabanqueApplication {
	
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ExabanqueApplication.class, args);
	}
	
	@Bean
	@LoadBalanced
	@Qualifier(REST_TEMPLATE_BEAN)
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	/** For swagger-ui **/
	@Bean
	public Docket banqueSwaggerApi(){
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.exakaconsulting.exabanque.web"))
				/*.paths(PathSelectors.regex("/*"))*/
				.build();
	}


}
