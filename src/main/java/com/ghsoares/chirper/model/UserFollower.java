package com.ghsoares.chirper.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="tb_user_follower")
public class UserFollower {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userFollowerId;
	
	@ManyToOne
	@JsonIgnoreProperties({ "chirps", "followers", "follows", "likes" })
	private User user;
	
	@ManyToOne
	@JsonIgnoreProperties({ "chirps", "followers", "follows", "likes" })
	private User follower;

	public Long getUserFollowerId() {
		return userFollowerId;
	}

	public User getUser() {
		return user;
	}

	public User getFollower() {
		return follower;
	}

	public void setUserFollowerId(Long userFollowerId) {
		this.userFollowerId = userFollowerId;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setFollower(User follower) {
		this.follower = follower;
	}
}
