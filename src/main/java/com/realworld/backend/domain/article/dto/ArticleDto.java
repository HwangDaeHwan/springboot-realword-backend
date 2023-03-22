package com.realworld.backend.domain.article.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.realworld.backend.domain.profile.dto.ProfileDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ArticleDto {

	private String slug;
	
	@NotNull
	private String title;
	@NotNull
	private String description;
	@NotNull
	private String body;
	private List<String> tagList;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Boolean favorited;
	private Long favoritesCount;
	private ProfileDto author;
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SinagleArticle<T> {
		private T article;
	}
	
	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MultipleArticle {
		private List<ArticleDto> articles;
	}
	
	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Update {
		private String title;
		private String description;
		private String body;
	}
}
