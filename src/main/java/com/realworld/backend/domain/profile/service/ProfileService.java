package com.realworld.backend.domain.profile.service;

import com.realworld.backend.domain.profile.dto.ProfileDto;
import com.realworld.backend.security.AuthUserDetails;

public interface ProfileService {
    ProfileDto getProfile(final String name, final AuthUserDetails authUserDetails);

    ProfileDto followUser(final String name, final AuthUserDetails authUserDetails);

    ProfileDto unfollowUser(final String name, final AuthUserDetails authUserDetails);

    ProfileDto getProfileByUserId(Long userId, AuthUserDetails authUserDetails);
}
