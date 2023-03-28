package com.realworld.backend.domain.profile.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realworld.backend.domain.profile.dto.ProfileDto;
import com.realworld.backend.domain.profile.service.ProfileService;
import com.realworld.backend.security.AuthUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfilesController {
	private final ProfileService profileService;
	
	@GetMapping("/{username}")
	public ProfileDto.Single getProfile(@PathVariable("username") String name,  @AuthenticationPrincipal AuthUserDetails authUserDetails) {
		return new ProfileDto.Single(profileService.getProfile(name, authUserDetails));
	}
	
    @PostMapping("/{username}/follow")
    public ProfileDto.Single followUser(@PathVariable("username") String name, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        return new ProfileDto.Single(profileService.followUser(name, authUserDetails));
    }

    @DeleteMapping("/{username}/follow")
    public ProfileDto.Single unfollowUser(@PathVariable("username") String name, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        return new ProfileDto.Single(profileService.unfollowUser(name, authUserDetails));
    }
}
