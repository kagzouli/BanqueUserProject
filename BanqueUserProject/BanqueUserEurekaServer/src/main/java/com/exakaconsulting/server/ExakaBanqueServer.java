package com.exakaconsulting.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.HystrixAutoConfiguration;

@SpringBootApplication(exclude={HystrixAutoConfiguration.class})
@EnableEurekaServer
public class ExakaBanqueServer {

	public static void main(String[] args) {
		SpringApplication.run(ExakaBanqueServer.class, args);
	}
}
