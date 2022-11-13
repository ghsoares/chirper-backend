package com.ghsoares.chirper.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

import com.ghsoares.chirper.model.Chirp;
import com.ghsoares.chirper.repository.ChirpRepository;
import com.ghsoares.chirper.repository.OffsetLimitPageable;
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
	
	@GetMapping(path = "/list")
	public ResponseEntity<List<Chirp>> getAll() {
		return ResponseEntity.ok(chirpRepository.findAll());
	}
	
	@GetMapping(path = "/list", params = {"start", "count"})
	public ResponseEntity<List<Chirp>> getAllRange(@RequestParam int start, @RequestParam int count) {
		return ResponseEntity.ok(chirpRepository.findAll(OffsetLimitPageable.of(start, count)).getContent());
	}
	
	@GetMapping(path = "/list", params = {"page", "count"})
	public ResponseEntity<List<Chirp>> getAllPage(@RequestParam int page, @RequestParam int count) {
		return ResponseEntity.ok(chirpRepository.findAll(PageRequest.of(page, count)).getContent());
	}
	
	@GetMapping(path = "/main")
	public ResponseEntity<List<Chirp>> getMain() {
		return ResponseEntity.ok(chirpRepository.findAllMain());
	}
	
	@GetMapping(path = "/find", params = {"id"})
	public ResponseEntity<Chirp> getById(@RequestParam Long id) {
		return chirpRepository.findById(id)
			.map(resp -> ResponseEntity.ok(resp))
			.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping(path = "/find", params = {"username"})
	public ResponseEntity<List<Chirp>> getAllByUser(@RequestParam String username){
		return ResponseEntity.ok(chirpRepository.findAllByAuthorUsername(username));
	}
	
	@GetMapping(path = "/find", params = {"tags"})
	public ResponseEntity<List<Chirp>> getAllByTags(@RequestParam List<String> tags){
		return ResponseEntity.ok(chirpRepository.findAllByTagsIn(tags));
	}
	
	@PostMapping(path = "/create")
	public ResponseEntity<Chirp> createChirp(@Valid @RequestBody Chirp chirp) {
		Optional<UserDetailsImpl> auth = SecurityUtils.getUserDetails();
		if (auth.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in", null);
		}
		return chirpService.createChirp(auth.get().getUserId(), chirp).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@PutMapping(path = "/update")
	public ResponseEntity<Chirp> updateChirp(@Valid @RequestBody Chirp chirp) {
		Optional<UserDetailsImpl> auth = SecurityUtils.getUserDetails();
		if (auth.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in", null);
		}
		return chirpService.updateChirp(auth.get().getUserId(), chirp)
				.map(resp -> ResponseEntity.status(HttpStatus.OK).body(resp))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@DeleteMapping(path = "/delete", params = {"id"})
	public ResponseEntity<Chirp> deleteById(@RequestParam Long id) {
		Optional<UserDetailsImpl> auth = SecurityUtils.getUserDetails();
		if (auth.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in", null);
		}
		return chirpService.deleteChirp(auth.get().getUserId(), id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping(path = "/delete/all")
	public void deleteAll() {
		chirpRepository.deleteAll();
	}
}