package com.exakaconsulting.banque.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BanqueSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Bean
	InMemoryUserDetailsManager userDetailsManager() {

		UserBuilder builder = User.withDefaultPasswordEncoder();

		UserDetails manager = builder.username("manager").password("baNkmaNager35#").roles("bankmanager").build();
		UserDetails collaborator = builder.username("collaborator").password("collaBoRator35#").roles("bankcollaborator").build();

		return new InMemoryUserDetailsManager(manager, collaborator);
	}
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
		http.csrf().disable();
		http.cors();
	}

}
