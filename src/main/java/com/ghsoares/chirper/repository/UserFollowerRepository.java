package com.ghsoares.chirper.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ghsoares.chirper.model.UserFollower;

@Repository
public interface UserFollowerRepository extends JpaRepository<UserFollower, Long> {
	public Optional<UserFollower> findByUserUserIdAndFollowerUserId(Long userId, Long followerId);
}
