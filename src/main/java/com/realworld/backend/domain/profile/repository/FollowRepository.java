package com.realworld.backend.domain.profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realworld.backend.domain.profile.entity.FollowEntity;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

	Optional<FollowEntity> findByFolloweeIdAndFollwerId(Long followeeId, Long follwerId);
	
	List<FollowEntity> findByFollowerId(Long id);
	
	List<FollowEntity> findByFollowerIdAndFolloweeIdIn(Long id, List<Long> authorIds);
}
