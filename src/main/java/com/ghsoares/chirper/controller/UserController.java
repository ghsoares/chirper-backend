package com.ghsoares.chirper.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ghsoares.chirper.model.User;
import com.ghsoares.chirper.model.ChirpLike;
import com.ghsoares.chirper.repository.UserRepository;
import com.ghsoares.chirper.security.SecurityUtils;
import com.ghsoares.chirper.security.UserDetailsImpl;
import com.ghsoares.chirper.service.ChirpService;
import com.ghsoares.chirper.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	public UserService userService;
	
	@Autowired
	public ChirpService chirpService;
	
	@GetMapping(path = "/list")
	public ResponseEntity<List<User>> getAll() {
		return ResponseEntity.ok(userRepository.findAll());
	}
	
	@GetMapping(path = "/find", params = {"id"})
	public ResponseEntity<User> getById(@RequestParam Long id) {
		return userRepository.findById(id)
			.map(resp -> ResponseEntity.ok(resp))
			.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping(path = "/find", params = {"name"})
	public ResponseEntity<List<User>> getAllByProfileName(@RequestParam String name){
		return ResponseEntity.ok(userRepository.findAllByProfileNameContainingIgnoreCase(name));
	}
	
	@GetMapping(path = "/find", params = {"username"})
	public ResponseEntity<User> getByUsername(@RequestParam String username){
		return userRepository.findByUsername(username)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping(path = "/find", params = {"email"})
	public ResponseEntity<User> getByEmail(@RequestParam String email){
		return userRepository.findByEmail(email)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping(path = "/login")
	public ResponseEntity<User> loginUser(@RequestBody User user) {
		return userService.authUser(user).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@PostMapping(path = "/register")
	public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
		return userService.registerUser(user)
				.map(resp -> ResponseEntity.status(HttpStatus.CREATED).body(resp))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}
	
	@PutMapping(path = "/update")
	public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
		Optional<UserDetailsImpl> auth = SecurityUtils.getUserDetails();
		if (auth.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in", null);
		}
		return userService.updateUser(auth.get().getUserId(), user)
			.map(resp -> ResponseEntity.status(HttpStatus.OK).body(resp))
			.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@PutMapping(path = "/like", params = {"chirp-id"})
	public ResponseEntity<ChirpLike> userLikeChirp(@RequestParam("chirp-id") Long id) {
		Optional<UserDetailsImpl> auth = SecurityUtils.getUserDetails();
		if (auth.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in", null);
		}
		return chirpService.likeChirp(auth.get().getUserId(), id)
				.map(resp -> ResponseEntity.status(HttpStatus.OK).body(resp))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@PutMapping(path = "/unlike", params = {"chirp-id"})
	public ResponseEntity<ChirpLike> userUnlikeChirp(@RequestParam("chirp-id") Long id) {
		Optional<UserDetailsImpl> auth = SecurityUtils.getUserDetails();
		if (auth.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in", null);
		}
		return chirpService.unlikeChirp(auth.get().getUserId(), id)
				.map(resp -> ResponseEntity.status(HttpStatus.OK).body(resp))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@DeleteMapping(path = "/delete", params = {"chirp-id"})
	public ResponseEntity<User> deleteById(@RequestParam("chirp-id") Long id) {
		return userService.deleteUser(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping(path = "/delete/all")
	public void deleteAll() {
		userRepository.deleteAll();
	}
}




