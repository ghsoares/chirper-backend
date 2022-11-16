package com.ghsoares.chirper.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ghsoares.chirper.model.User;
import com.ghsoares.chirper.model.UserFollower;
import com.ghsoares.chirper.repository.UserFollowerRepository;
import com.ghsoares.chirper.repository.UserRepository;
import com.ghsoares.chirper.security.SecurityUtils;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserFollowerRepository userFollowerRepository;
	
	public Optional<User> registerUser(User user) {
		if (userRepository.findTopByUsernameOrEmail(user.getUsername(), user.getEmail())
				.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exist", null);
		}
		user.setPassword(SecurityUtils.encodeString(user.getPassword()));
		user.setBio(user.getProfileName() + " is new to Chirper!");
		return Optional.of(userRepository.save(user));
	}
	
	public Optional<User> updateUser(Long userId, User user) {
		Optional<User> prevUser = userRepository.findById(userId);
		
		if (prevUser.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist", null);
		}
		
		Optional<User> searchUser = userRepository.findTopByUsernameOrEmail(user.getUsername(),
				user.getEmail());

		if (searchUser.isPresent() && (searchUser.get().getUserId() != userId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exist", null);
		}
		
		if (user.getProfileName() != null) {
			prevUser.get().setProfileName(user.getProfileName());
		}
		if (user.getUsername() != null) {
			prevUser.get().setUsername(user.getUsername());
		}
		if (user.getEmail() != null) {
			prevUser.get().setEmail(user.getEmail());
		}
		if (user.getBirthDate() != null) {
			prevUser.get().setBirthDate(user.getBirthDate());
		}
		if (user.getBio() != null) {
			prevUser.get().setBio(user.getBio());
		}

		return Optional.of(userRepository.save(prevUser.get()));
	}
	
	public Optional<User> updateUserPassword(Long userId, String newPassword) {
		Optional<User> prevUser = userRepository.findById(userId);
		
		if (prevUser.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist", null);
		}
		
		User user = prevUser.get();
		
		user.setToken(SecurityUtils.generateBasicToken(user.getUsername(), newPassword));
		user.setPassword(SecurityUtils.encodeString(newPassword));
		return Optional.of(userRepository.save(user));
	}
	
	public Optional<User> authUser(User loginInfo) {
		Optional<User> user = userRepository.findTopByUsernameOrEmail(loginInfo.getUsername(), loginInfo.getUsername());
		if (user.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password", null);
		}
		
		if (!SecurityUtils.compareStrings(loginInfo.getPassword(), user.get().getPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password", null);
		}
		
		String token = SecurityUtils.generateBasicToken(loginInfo.getUsername(), loginInfo.getPassword());
		
		user.get().setToken(token);

		return user;
	}
	
	public Optional<UserFollower> followUser(Long userId, Long followUserId) {
		Optional<User> user = userRepository.findById(followUserId);
		if (user.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist", null);
		}
		
		if (userFollowerRepository.findByUserUserIdAndFollowerUserId(followUserId, userId).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already follows this user", null);
		}
		
		UserFollower follow = new UserFollower();
		follow.setUser(user.get());
		follow.setFollower(new User(userId));
		return Optional.of(userFollowerRepository.save(follow));
	}
	
	public Optional<UserFollower> unfollowUser(Long userId, Long followUserId) {
		Optional<UserFollower> follow = userFollowerRepository.findByUserUserIdAndFollowerUserId(followUserId, userId);
		
		if (follow.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either user or follower doesn't exist", null);
		}
		
		userFollowerRepository.deleteById(follow.get().getUserFollowerId());
		return follow;
	}
	
	public Optional<User> deleteUser(Long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist", null);
		}
		userRepository.deleteById(id);
		return user;
	}
	
	
	
}
