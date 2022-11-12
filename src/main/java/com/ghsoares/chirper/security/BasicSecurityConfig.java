package com.ghsoares.chirper.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);

		auth.inMemoryAuthentication()
			.withUser("root")
			.password(passwordEncoder().encode("root"))
			.authorities("USER", "ADMIN");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
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
			
			.and().httpBasic().and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().cors()
			.and().csrf().disable();
	}
}
