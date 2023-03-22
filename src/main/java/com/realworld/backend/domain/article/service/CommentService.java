package com.realworld.backend.domain.article.service;

import java.util.List;

import com.realworld.backend.domain.article.dto.CommentDto;
import com.realworld.backend.security.AuthUserDetails;

public interface CommentService {

    CommentDto addCommentsToAnArticle(final String slug, final CommentDto comment, final AuthUserDetails authUserDetails);

    void delete(final String slug, final Long commentId, final AuthUserDetails authUserDetails);

    List<CommentDto> getCommentsBySlug(final String slug, final AuthUserDetails authUserDetails);
}
