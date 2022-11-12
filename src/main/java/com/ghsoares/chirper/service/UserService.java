package com.ghsoares.chirper.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ghsoares.chirper.model.User;
import com.ghsoares.chirper.repository.UserRepository;
import com.ghsoares.chirper.security.SecurityUtils;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public Optional<User> registerUser(User user) {
		if (userRepository.findTopByUsernameOrEmail(user.getUsername(), user.getEmail())
				.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exist", null);
		}
		user.setPassword(SecurityUtils.encodeString(user.getPassword()));
		return Optional.of(userRepository.save(user));
	}
	
	public Optional<User> updateUser(Long userId, User user) {
		if (userId != user.getUserId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trying to update a different user", null);
		}
		
		if (userRepository.findById(user.getUserId()).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist", null);
		}
		
		Optional<User> searchUser = userRepository.findTopByUsernameOrEmail(user.getUsername(),
				user.getEmail());

		if (searchUser.isPresent() && (searchUser.get().getUserId() != user.getUserId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exist", null);
		}

		user.setPassword(SecurityUtils.encodeString(user.getPassword()));
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
	
	public Optional<User> deleteUser(Long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist", null);
		}
		userRepository.deleteById(id);
		return user;
	}
	
	
	
}
