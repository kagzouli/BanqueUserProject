package com.exakaconsulting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude= {MongoAutoConfiguration.class , MongoDataAutoConfiguration.class})
public class BanqueApplication{
		

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BanqueApplication.class, args);
	}

}
