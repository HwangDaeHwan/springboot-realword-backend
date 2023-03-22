package com.realworld.backend.domain.article.service;

import java.util.List;

import com.realworld.backend.domain.article.dto.ArticleDto;
import com.realworld.backend.domain.article.model.ArticleQueryParam;
import com.realworld.backend.domain.article.model.FeedParams;
import com.realworld.backend.security.AuthUserDetails;

public interface ArticleService {
	
	ArticleDto createArticle(final ArticleDto article, final AuthUserDetails authUserDetails);

    ArticleDto getArticle(final String slug, final AuthUserDetails authUserDetails);

    ArticleDto updateArticle(final String slug, final ArticleDto.Update article, final AuthUserDetails authUserDetails);

    void deleteArticle(final String slug, final AuthUserDetails authUserDetails);

    List<ArticleDto> feedArticles(final AuthUserDetails authUserDetails, final FeedParams feedParams);

    ArticleDto favoriteArticle(final String slug, final AuthUserDetails authUserDetails);

    ArticleDto unfavoriteArticle(final String slug, final AuthUserDetails authUserDetails);

    List<ArticleDto> listArticle(final ArticleQueryParam articleQueryParam, final AuthUserDetails authUserDetails);
}
