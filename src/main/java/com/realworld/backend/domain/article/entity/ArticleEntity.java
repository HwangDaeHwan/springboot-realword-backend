package com.realworld.backend.domain.article.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.realworld.backend.domain.common.entity.BaseEntity;
import com.realworld.backend.domain.tag.entity.ArticleTagRelationEntity;
import com.realworld.backend.domain.user.entity.UserEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "articles")
@NamedEntityGraph(name = "fetch-author-tagList", 
	attributeNodes = { @NamedAttributeNode("author"), @NamedAttributeNode("tagList") })
public class ArticleEntity extends BaseEntity {

	@Column(nullable = false)
	private String slug;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	private String body;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private UserEntity author;
	
	@OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ArticleTagRelationEntity> tagList;
	
	@OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FavoriteEntity> favoriteList;
	
	@Builder
	public ArticleEntity(Long id, String slug, String title, String description, 
			String body, UserEntity author) {
		this.id = id;
		this.slug = slug;
		this.title = title;
		this.description = description;
		this.body = body;
		this.author = author;
		this.tagList = new ArrayList<>();
		
	}
}
