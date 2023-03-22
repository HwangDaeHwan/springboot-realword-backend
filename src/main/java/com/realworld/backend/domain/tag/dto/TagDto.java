package com.realworld.backend.domain.tag.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TagDto {

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TagList {
		List<String> tags;
	}
}
