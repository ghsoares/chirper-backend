package com.ghsoares.chirper.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class BasicSecurityConfig {
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
		
		auth.inMemoryAuthentication()
			.withUser("root")
			.password(passwordEncoder().encode("root"))
			.authorities("USER", "ADMIN");
		
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
			
		return auth.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/user/login").permitAll()
			.antMatchers("/user/register").permitAll()
			.antMatchers("/user/update").hasAuthority("USER")
			
			.antMatchers("/user/like/{chirpId}").hasAuthority("USER")
			.antMatchers("/user/unlike/{chirpId}").hasAuthority("USER")
			
			.antMatchers("/chirp/all").permitAll()
			.antMatchers("/chirp/by-user/{username}").permitAll()
			.antMatchers("/chirp/by-tags/{tags}").permitAll()
			.antMatchers("/chirp/delete").hasAuthority("USER")
			
			.antMatchers("/chirp/create").hasAuthority("USER")
//			.antMatchers("/chirp/reply").hasAuthority("USER")
			.antMatchers("/chirp/update").hasAuthority("USER")
			.antMatchers("/chirp/delete").hasAuthority("USER")
			
			.anyRequest().authenticated()
			
			.and().authenticationManager(authenticationManager(http))
			
			.httpBasic().and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().cors()
			.and().csrf().disable();
		
		return http.build();
	}
}
