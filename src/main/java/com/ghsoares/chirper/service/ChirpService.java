package com.ghsoares.chirper.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ghsoares.chirper.model.Chirp;
import com.ghsoares.chirper.model.ChirpLike;
import com.ghsoares.chirper.model.User;
import com.ghsoares.chirper.repository.ChirpLikeRepository;
import com.ghsoares.chirper.repository.ChirpRepository;
import com.ghsoares.chirper.repository.UserRepository;

@Service
public class ChirpService {
	@Autowired
	private ChirpRepository chirpRepository;
	
	@Autowired
	private ChirpLikeRepository chirpLikeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Optional<Chirp> createChirp(Long userId, Chirp chirp) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist", null);
		}
		chirp.setAuthor(user.get());
		chirp.setCreationDate(LocalDate.now());
		chirp.setEditDate(chirp.getCreationDate());
		chirp.setTags(getBodyTags(chirp.getBody()));
		return Optional.of(chirpRepository.save(chirp));
	}
	
	public Optional<Chirp> updateChirp(Long userId, Chirp chirp) {
		if (userId != chirp.getAuthor().getUserId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trying to update a chirp from a different user", null);
		}
		
		if (chirpRepository.findById(chirp.getChirpId()).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chirp doesn't exist", null);
		}
		
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist", null);
		}
		
		chirp.setEditDate(LocalDate.now());
		return Optional.of(chirpRepository.save(chirp));
	}
	
	public Optional<ChirpLike> likeChirp(Long userId, Long chirpId) {
		Optional<Chirp> chirp = chirpRepository.findById(chirpId);
		if (chirp.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chirp doesn't exist", null);
		}
		
		if (chirpLikeRepository.findByUserUserIdAndChirpChirpId(userId, chirpId).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already liked this chirp", null);
		}
		
		ChirpLike like = new ChirpLike();
		like.setChirp(chirp.get());
		like.setUser(new User(userId));
		return Optional.of(chirpLikeRepository.save(like));
	}
	
	public Optional<ChirpLike> unlikeChirp(Long userId, Long chirpId) {
		Optional<ChirpLike> like = chirpLikeRepository.findByUserUserIdAndChirpChirpId(userId, chirpId);
		
		if (like.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either user or chirp doesn't exist", null);
		}
		
		chirpLikeRepository.deleteById(like.get().getLikeId());
		return like;
	}
	
	public Optional<Chirp> deleteChirp(Long userId, Long id) {
		Optional<Chirp> chirp = chirpRepository.findById(id);
		if (chirp.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chirp doesn't exist", null);
		}
		
		if (userId != chirp.get().getAuthor().getUserId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trying to delete a chirp from a different user", null);
		}
		
		chirpRepository.deleteById(id);
		return chirp;
	}

	private Set<String> getBodyTags(String body) {
		Pattern pattern = Pattern.compile("#(\\w+)");
		Matcher matcher = pattern.matcher(body);
		
		HashSet<String> tags = new HashSet<String>();
		
		while (matcher.find()) {
			tags.add(matcher.group(1));
		}
		
		return tags;
	}
}
