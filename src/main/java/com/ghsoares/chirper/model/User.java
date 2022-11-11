package com.ghsoares.chirper.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@NotNull(message = "The name can't be null")
	private String name;
	
	@NotNull(message = "The username can't be null")
	private String username;
	
	@NotNull(message = "The email can't be null")
	private String email;
	
	@NotNull(message = "The password can't be null")
	private String password;
}







