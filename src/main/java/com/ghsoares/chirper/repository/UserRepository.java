package com.ghsoares.chirper.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ghsoares.chirper.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	public Optional<User> findTopByUsernameOrEmail(String username, String email);
	public Optional<User> findByUsername(String username);
	public Optional<User> findByEmail(String email);
	public List<User> findAllByNameContainingIgnoreCase(String name);
	public List<User> findAllByUsernameContainingIgnoreCase(String username);
	
}
