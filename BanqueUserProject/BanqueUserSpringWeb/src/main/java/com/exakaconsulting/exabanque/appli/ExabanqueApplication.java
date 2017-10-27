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

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.exakaconsulting.exabanque.*")
@SpringBootApplication
@EnableEurekaClient
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


}
