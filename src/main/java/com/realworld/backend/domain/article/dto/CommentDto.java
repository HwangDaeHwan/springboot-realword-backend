package com.realworld.backend.domain.article.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.realworld.backend.domain.profile.dto.ProfileDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

	private Long id;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@NotNull
	private String body;
	private ProfileDto author;
	
	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SingleComment {
		CommentDto comment;
	}
	
	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MultipleComments {
		List<CommentDto> comments;
	}
}
