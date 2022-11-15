package com.ghsoares.chirper.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ghsoares.chirper.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	public Optional<User> findTopByUsernameOrEmail(String username, String email);
	public Optional<User> findByUsername(String username);
	public Optional<User> findByEmail(String email);
	public Page<User> findAllByProfileNameContainingIgnoreCase(String profileName, Pageable pageable);
	public Page<User> findAllByUsernameContainingIgnoreCase(String username, Pageable pageable);
	@Query("SELECT u FROM User u WHERE LOWER(u.profileName) LIKE CONCAT('%',LOWER(:query),'%') OR LOWER(u.username) LIKE CONCAT('%',LOWER(:query),'%')")
	public Page<User> findAllByQuery(String query, Pageable pageable);
}
