package com.realworld.backend.domain.article.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realworld.backend.domain.article.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

	@EntityGraph("fetch-author")
	List<CommentEntity> findByArticleId(Long articleId);
}
