package com.ghsoares.chirper.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ghsoares.chirper.model.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Long> {
	public Page<Chirp> findAllByAuthorUsernameContainingIgnoreCase(String username, Pageable pageable);
	public Page<Chirp> findAllByTagsIn(List<String> tags, Pageable pageable);
	
	@Query("SELECT c FROM Chirp c WHERE replyOf IS NULL")
	public Page<Chirp> findAllNotReply(Pageable pageable);
	@Query("SELECT c FROM Chirp c WHERE LOWER(c.author.profileName) LIKE CONCAT('%',LOWER(:query),'%') OR LOWER(c.author.username) LIKE CONCAT('%',LOWER(:query),'%') OR LOWER(c.body) LIKE CONCAT('%',LOWER(:query),'%')")
	public Page<Chirp> findAllByQuery(String query, Pageable pageable);
}



