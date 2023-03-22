package com.realworld.backend.domain.tag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.realworld.backend.domain.article.entity.ArticleEntity;
import com.realworld.backend.domain.common.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class ArticleTagRelationEntity extends BaseEntity {

	@Column(nullable = false, name = "tag")
	private String tag;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "article")
	private ArticleEntity article;
}
