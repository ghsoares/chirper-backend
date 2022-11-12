package com.ghsoares.chirper.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ghsoares.chirper.model.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Long> {
	public List<Chirp> findAllByAuthorUsername(String username);
	public List<Chirp> findAllByTagsIn(List<String> tags);
	@Query("Select COUNT(cl) FROM ChirpLike cl WHERE cl.chirp.chirpId=chirpId")
	public Long getLikeCount(Long chirpId);
}
