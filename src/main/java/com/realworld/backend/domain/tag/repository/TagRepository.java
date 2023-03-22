package com.realworld.backend.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realworld.backend.domain.tag.entity.ArticleTagRelationEntity;

@Repository
public interface TagRepository extends JpaRepository<ArticleTagRelationEntity, Long> {
}
