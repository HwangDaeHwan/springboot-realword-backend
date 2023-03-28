package com.realworld.backend.domain.article.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.realworld.backend.domain.article.dto.ArticleDto;
import com.realworld.backend.domain.article.dto.CommentDto;
import com.realworld.backend.domain.article.entity.ArticleEntity;
import com.realworld.backend.domain.article.entity.CommentEntity;
import com.realworld.backend.domain.article.repository.ArticleRepository;
import com.realworld.backend.domain.article.repository.CommentRepository;
import com.realworld.backend.domain.profile.dto.ProfileDto;
import com.realworld.backend.domain.profile.service.ProfileService;
import com.realworld.backend.domain.user.entity.UserEntity;
import com.realworld.backend.security.AuthUserDetails;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {
    CommentServiceImpl commentService;

    @Mock
    ArticleRepository articleRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    ProfileService profileService;

    AuthUserDetails authUserDetails;
    ArticleDto article;
    String expectedSlug;

    UserEntity author;
    ArticleEntity expectedArticle;
    CommentEntity expectedComment;

    ProfileDto authorProfile;

    @BeforeEach
    void setUp() {
        commentService = new CommentServiceImpl(articleRepository, commentRepository, profileService);

        authUserDetails = AuthUserDetails.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        article = ArticleDto.builder()
                .title("article title")
                .description("description")
                .body("hi there")
                .tagList(List.of("tag1", "tag2"))
                .favoritesCount(0L)
                .favorited(false)
                .build();

        expectedSlug = String.join("-", article.getTitle().split(" "));

        author = UserEntity.builder()
                .id(authUserDetails.getId())
                .email(authUserDetails.getEmail())
                .username("username")
                .build();

        authorProfile = ProfileDto.builder()
                .username("username")
                .following(true)
                .build();

        expectedArticle = ArticleEntity.builder()
                .id(1L)
                .slug(expectedSlug)
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .author(author)
                .build();

        expectedComment = CommentEntity.builder()
                .body("body")
                .author(UserEntity.builder()
                        .id(authUserDetails.getId())
                        .build())
                .article(expectedArticle)
                .build();
    }

    @Test
    void whenCommentForArticleSlug_thenReturnComment() {
        CommentDto commentDto = CommentDto.builder().body("body").build();

        when(articleRepository.findBySlug(eq(expectedSlug))).thenReturn(Optional.of(expectedArticle));
        when(profileService.getProfileByUserId(eq(author.getId()), any())).thenReturn(authorProfile);

        CommentDto actual = commentService.addCommentsToAnArticle(expectedSlug, commentDto, authUserDetails);

        assertEquals(commentDto.getBody(), actual.getBody());
        assertEquals(author.getUsername(), actual.getAuthor().getUsername());
    }

    @Test
    void whenDeleteCommentIdArticleSlug_thenDelete() {
        Long commentId = 1L;

        when(articleRepository.findBySlug(eq(expectedSlug))).thenReturn(Optional.of(expectedArticle));
        when(commentRepository.findById(eq(commentId))).thenReturn(Optional.of(expectedComment));

        commentService.delete(expectedSlug, commentId, authUserDetails);

        verify(articleRepository, times(1)).findBySlug(eq(expectedSlug));
        verify(commentRepository, times(1)).findById(eq(commentId));
        verify(commentRepository, times(1)).delete(eq(expectedComment));
    }
}
