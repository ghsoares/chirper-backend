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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ghsoares.chirper.model.Chirp;
import com.ghsoares.chirper.repository.ChirpRepository;
import com.ghsoares.chirper.security.SecurityUtils;
import com.ghsoares.chirper.security.UserDetailsImpl;
import com.ghsoares.chirper.service.ChirpService;

@RestController
@RequestMapping("/chirp")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChirpController {
	@Autowired
	private ChirpRepository chirpRepository;
	
	@Autowired
	public ChirpService chirpService;
	
	@GetMapping("/all")
	public ResponseEntity<List<Chirp>> getAll() {
		return ResponseEntity.ok(chirpRepository.findAll());
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Chirp> getById(@PathVariable Long id) {
		return chirpRepository.findById(id)
			.map(resp -> ResponseEntity.ok(resp))
			.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/by-user/{username}")
	public ResponseEntity<List<Chirp>> getAllByUser(@PathVariable String username){
		return ResponseEntity.ok(chirpRepository.findAllByAuthorUsername(username));
	}
	
	@GetMapping("/by-tags/{tags}")
	public ResponseEntity<List<Chirp>> getAllByTags(@PathVariable List<String> tags){
		return ResponseEntity.ok(chirpRepository.findAllByTagsIn(tags));
	}
	
	@PostMapping("/create")
	public ResponseEntity<Chirp> createChirp(@Valid @RequestBody Chirp chirp) {
		Optional<UserDetailsImpl> auth = SecurityUtils.getUserDetails();
		if (auth.isPresent()) {
			return chirpService.createChirp(auth.get().getUserId(), chirp).map(resp -> ResponseEntity.ok(resp))
					.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
	
	@PutMapping("/update")
	public ResponseEntity<Chirp> updateChirp(@Valid @RequestBody Chirp chirp) {
		Optional<UserDetailsImpl> auth = SecurityUtils.getUserDetails();
		if (auth.isPresent()) {
			return chirpService.updateChirp(auth.get().getUserId(), chirp)
					.map(resp -> ResponseEntity.status(HttpStatus.OK).body(resp))
					.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Chirp> deleteById(@PathVariable Long id) {
		Optional<UserDetailsImpl> auth = SecurityUtils.getUserDetails();
		if (auth.isPresent()) {
			return chirpService.deleteChirp(auth.get().getUserId(), id)
					.map(resp -> ResponseEntity.ok(resp))
					.orElse(ResponseEntity.notFound().build());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}