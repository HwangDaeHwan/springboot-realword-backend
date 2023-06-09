package com.realworld.backend.domain.article.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realworld.backend.domain.article.entity.FavoriteEntity;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
	
	Optional<FavoriteEntity> findByArticleIdAndUserId(Long articleId, Long userId);
}
