package com.karol.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.karol.services.UserServiceImpl;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;
	
	UserServiceImpl userDetailsService=new UserServiceImpl();
	
	 @Bean
	    public  ShaPasswordEncoder  passwordEncoder() {
	        return new  ShaPasswordEncoder();
	    }
	 
	
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		
	
	
	  auth
	  .jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery(
			"select indexNumber,password, enabled from users where indexNumber=?")
		.authoritiesByUsernameQuery(
			"select indexNumber, role from user_roles where indexNumber=?");
	  
	}	
	//.passwordEncoder(passwordEncoder())
	
	
	     
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	       
	      http
	      .authorizeRequests()
	        .antMatchers("/", "/home", "/reservation", "/userpanel", "/reserved", "/userpanel/remove_reservation")
	        .access("hasRole('USER')")
	        .antMatchers("/admin/**").access("hasRole('ADMIN')")
	        .antMatchers("/db/**").access("hasRole('ADMIN') or hasRole('DBA')")
	        .and().formLogin().loginPage("/login")
	        .usernameParameter("indexNumber").passwordParameter("password")
	        .and().csrf()
	        .and().exceptionHandling().accessDeniedPage("/Access_Denied");
	    }

	   
	    
	  
}



