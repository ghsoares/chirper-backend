package com.ghsoares.chirper.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tb_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@NotNull(message = "The profile name can't be null")
	private String profileName;
	
	@NotNull(message = "The username can't be null")
	private String username;
	
	private String bio;
	
	@NotNull(message = "The email can't be null")
	private String email;
	
	@NotNull(message = "The password can't be null")
	private String password;
	
	@NotNull(message = "The birth date can't be null")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({ "author" })
	private List<Chirp> chirps = new ArrayList<Chirp>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({ "user", "chirp" })
	private List<ChirpLike> likes = new ArrayList<ChirpLike>();
	
	@Transient
	private String token;

	public User() {}

	public User(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public String getProfileName() {
		return profileName;
	}

	public String getUsername() {
		return username;
	}

	public String getBio() {
		return bio;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public List<Chirp> getChirps() {
		return chirps;
	}

	public List<ChirpLike> getLikes() {
		return likes;
	}

	public String getToken() {
		return token;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public void setChirps(List<Chirp> chirps) {
		this.chirps = chirps;
	}

	public void setLikes(List<ChirpLike> likes) {
		this.likes = likes;
	}

	public void setToken(String token) {
		this.token = token;
	}
}







