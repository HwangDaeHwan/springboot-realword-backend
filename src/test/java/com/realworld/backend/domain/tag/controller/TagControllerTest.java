package com.realworld.backend.domain.tag.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import com.realworld.backend.configuration.WithAuthUser;
import com.realworld.backend.domain.tag.service.TagService;
import com.realworld.backend.security.JWTAuthFilter;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TagController.class)
public class TagControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    JWTAuthFilter jwtAuthFilter;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    TagService tagService;

    @Test
    @WithAuthUser
    void whenThereAreTags_returnTags() throws Exception {
        when(tagService.listOfTags()).thenReturn(List.of("a", "b"));

        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags[0]", Matchers.is("a")));
    }
}
