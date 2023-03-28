package com.realworld.backend.domain.profile.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.realworld.backend.domain.profile.dto.ProfileDto;
import com.realworld.backend.domain.profile.entity.FollowEntity;
import com.realworld.backend.domain.profile.repository.FollowRepository;
import com.realworld.backend.domain.user.entity.UserEntity;
import com.realworld.backend.domain.user.repository.UserRepository;
import com.realworld.backend.exception.AppException;
import com.realworld.backend.exception.Error;
import com.realworld.backend.security.AuthUserDetails;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceImplTest {
    ProfileService profileService;

    AuthUserDetails authUserDetails;

    @Mock
    UserRepository userRepository;

    @Mock
    FollowRepository followRepository;

    private UserEntity expectedUser;

    @BeforeEach
    void setUp() {
        profileService = new ProfileServiceImpl(userRepository, followRepository);
        authUserDetails = AuthUserDetails.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        expectedUser = UserEntity.builder()
                .id(2L)
                .username("expectedUser")
                .email("expected@email.com")
                .bio("expected bio")
                .image("expected_image_path")
                .build();

        when(userRepository.findByUsername(eq(expectedUser.getUsername()))).thenReturn(Optional.of(expectedUser));
    }

    @Test
    void whenValidUsername_thenReturnProfile() {
        ProfileDto actual = profileService.getProfile(expectedUser.getUsername(), authUserDetails);

        assertEquals(expectedUser.getUsername(), actual.getUsername());
        assertEquals(expectedUser.getBio(), actual.getBio());
        assertEquals(expectedUser.getImage(), actual.getImage());
    }

    @Test
    void whenFollowValidUsername_thenFollowAndReturnProfile() {
        ProfileDto actual = profileService.followUser(expectedUser.getUsername(), authUserDetails);

        assertTrue(actual.getFollowing());
    }

    @Test
    void whenFollowFollowedUsername_thenThrow422() {
        when(followRepository.findByFolloweeIdAndFollowerId(expectedUser.getId(), authUserDetails.getId())).thenReturn(Optional.of(FollowEntity.builder().build()));
        try {
            profileService.followUser(expectedUser.getUsername(), authUserDetails);
            fail();
        } catch (AppException e) {
            assertEquals(Error.ALREADY_FOLLOWED_USER, e.getError());
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    void whenUnfollowFollowedUsername_thenReturnProfile() {
        when(followRepository.findByFolloweeIdAndFollowerId(expectedUser.getId(), authUserDetails.getId())).thenReturn(Optional.of(FollowEntity.builder().build()));

        ProfileDto actual = profileService.unfollowUser(expectedUser.getUsername(), authUserDetails);

        assertFalse(actual.getFollowing());
    }

    @Test
    void whenUnfollowNotFollowedUsername_thenThrow404() {
        try {
            profileService.unfollowUser(expectedUser.getUsername(), authUserDetails);
            fail();
        } catch (AppException e) {
            assertEquals(Error.FOLLOW_NOT_FOUND, e.getError());
        } catch (Exception e) {
            fail();
        }
    }
}
