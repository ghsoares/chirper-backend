package com.ghsoares.chirper.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public ResponseEntity<List<Chirp>> listAll(
		@RequestParam("start") Optional<Integer> start, 
		@RequestParam("count") Optional<Integer> count
	) {
		Pageable pageable = getPageable(start, count);
		Page<Chirp> chirps = chirpRepository.findAll(pageable);
		return ResponseEntity.ok(chirps.getContent());
	}
	
	@GetMapping(path = "/not-reply")
	public ResponseEntity<List<Chirp>> getNotReply(
		@RequestParam("start") Optional<Integer> start, 
		@RequestParam("count") Optional<Integer> count
	) {
		Pageable pageable = getPageable(start, count);
		Page<Chirp> chirps = chirpRepository.findAllNotReply(pageable);
		return ResponseEntity.ok(chirps.getContent());
	}
	
	@GetMapping(path = "/search", params = { "username" })
	public ResponseEntity<List<Chirp>> searchByAuthorUsername(
		@RequestParam("username") String username,
		@RequestParam("start") Optional<Integer> start, 
		@RequestParam("count") Optional<Integer> count
	) {
		Pageable pageable = getPageable(start, count);
		Page<Chirp> chirps = chirpRepository
				.findAllByAuthorUsernameContainingIgnoreCase(username, pageable);
		return ResponseEntity.ok(chirps.getContent());
	}
	
	@GetMapping(path = "/search", params = { "tags" })
	public ResponseEntity<List<Chirp>> searchByTags(
		@RequestParam("tags") List<String> tags,
		@RequestParam("start") Optional<Integer> start, 
		@RequestParam("count") Optional<Integer> count
	) {
		Pageable pageable = getPageable(start, count);
		Page<Chirp> chirps = chirpRepository.findAllByTagsIn(tags, pageable);
		return ResponseEntity.ok(chirps.getContent());
	}
	
	@GetMapping(path = "/search", params = { "query" })
	public ResponseEntity<List<Chirp>> searchByQuery(
		@RequestParam("query") String query,
		@RequestParam("start") Optional<Integer> start, 
		@RequestParam("count") Optional<Integer> count
	) {
		Pageable pageable = getPageable(start, count);
		Page<Chirp> chirps = chirpRepository.findAllByQuery(query, pageable);
		return ResponseEntity.ok(chirps.getContent());
	}
	
	@GetMapping(path = "/find", params = { "id" })
	public ResponseEntity<Chirp> getById(@RequestParam Long id) {
		return chirpRepository.findById(id)
			.map(resp -> ResponseEntity.ok(resp))
			.orElse(ResponseEntity.notFound().build());
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
	
	@DeleteMapping(path = "/delete", params = { "id" })
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
	
	private Pageable getPageable(
		Optional<Integer> offset, 
		Optional<Integer> count
	) {
		Pageable pageable = Pageable.unpaged();
		if (offset.isPresent() || count.isPresent()) {
			pageable = OffsetLimitPageable.of(offset.orElse(0), count.orElse(5));
		}
		return pageable;
	}
	
}




