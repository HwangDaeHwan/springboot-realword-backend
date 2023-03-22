package com.realworld.backend.domain.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {

	private String username;
	private String bio;
	private String image;
	private Boolean following;
	
	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Single {
		private ProfileDto profile;
	}
}
