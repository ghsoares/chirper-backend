package com.ghsoares.chirper.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="tb_chirp_like")
public class ChirpLike {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long likeId;
	
	@ManyToOne
	@JsonIgnoreProperties({ "chirps", "likes" })
	private User user;
	
	@ManyToOne
	@JsonIgnoreProperties({ "replies", "likes" })
	private Chirp chirp;

	public Long getLikeId() {
		return likeId;
	}

	public User getUser() {
		return user;
	}

	public Chirp getChirp() {
		return chirp;
	}

	public void setLikeId(Long likeId) {
		this.likeId = likeId;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setChirp(Chirp chirp) {
		this.chirp = chirp;
	}

	
}
