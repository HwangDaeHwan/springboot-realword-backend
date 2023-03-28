package com.realworld.backend.domain.profile.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import com.realworld.backend.configuration.WithAuthUser;
import com.realworld.backend.domain.profile.dto.ProfileDto;
import com.realworld.backend.domain.profile.service.ProfileService;
import com.realworld.backend.security.AuthUserDetails;
import com.realworld.backend.security.JWTAuthFilter;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProfilesController.class)
public class ProfilesControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    JWTAuthFilter jwtAuthFilter;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    ProfileService profileService;

    @Test
    @WithAuthUser
    void whenValidUsername_thenReturnProfile() throws Exception {

        ProfileDto profileDto = ProfileDto.builder()
                .username("testUser")
                .bio("some bio")
                .image("profilephoto")
                .following(false)
                .build();

        when(profileService.getProfile(eq("testUser"), any(AuthUserDetails.class))).thenReturn(profileDto);

        mockMvc.perform(get("/profiles/testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile", Matchers.notNullValue(ProfileDto.class)));
    }

    @Test
    @WithAuthUser
    void whenFollowValidUsername_thenReturnProfile() throws Exception {
        ProfileDto profileDto = ProfileDto.builder()
                .username("testUser")
                .bio("some bio")
                .image("profilephoto")
                .following(true)
                .build();

        when(profileService.followUser(eq("testUser"), any(AuthUserDetails.class))).thenReturn(profileDto);

        mockMvc.perform(post("/profiles/testUser/follow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile", Matchers.notNullValue(ProfileDto.class)))
                .andExpect(jsonPath("$.profile.following", Matchers.is(true)));
    }

    @Test
    @WithAuthUser
    void whenUnFollowFollowedUsername_thenReturnProfile() throws Exception {
        ProfileDto profileDto = ProfileDto.builder()
                .username("testUser")
                .bio("some bio")
                .image("profilephoto")
                .following(true)
                .build();

        when(profileService.unfollowUser(eq("testUser"), any(AuthUserDetails.class))).thenReturn(profileDto);

        mockMvc.perform(delete("/profiles/testUser/follow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile", Matchers.notNullValue(ProfileDto.class)))
                .andExpect(jsonPath("$.profile.following", Matchers.is(true)));
    }
}
