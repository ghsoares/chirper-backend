package com.ghsoares.chirper.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ghsoares.chirper.model.ChirpLike;

@Repository
public interface ChirpLikeRepository extends JpaRepository<ChirpLike, Long> {
	public Optional<ChirpLike> findByUserUserIdAndChirpChirpId(Long userId, Long chirpId);
}
