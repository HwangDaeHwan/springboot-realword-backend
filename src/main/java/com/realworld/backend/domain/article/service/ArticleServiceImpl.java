package com.realworld.backend.domain.article.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.realworld.backend.domain.article.dto.ArticleDto;
import com.realworld.backend.domain.article.dto.ArticleDto.Update;
import com.realworld.backend.domain.article.entity.ArticleEntity;
import com.realworld.backend.domain.article.entity.FavoriteEntity;
import com.realworld.backend.domain.article.model.ArticleQueryParam;
import com.realworld.backend.domain.article.model.FeedParams;
import com.realworld.backend.domain.article.repository.ArticleRepository;
import com.realworld.backend.domain.article.repository.FavoriteRepository;
import com.realworld.backend.domain.common.entity.BaseEntity;
import com.realworld.backend.domain.profile.dto.ProfileDto;
import com.realworld.backend.domain.profile.entity.FollowEntity;
import com.realworld.backend.domain.profile.repository.FollowRepository;
import com.realworld.backend.domain.profile.service.ProfileService;
import com.realworld.backend.domain.tag.entity.ArticleTagRelationEntity;
import com.realworld.backend.domain.user.entity.UserEntity;
import com.realworld.backend.exception.AppException;
import com.realworld.backend.exception.Error;
import com.realworld.backend.security.AuthUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final FollowRepository followRepository;
    private final FavoriteRepository favoriteRepository;
    private final ProfileService profileService;
	
    private ArticleDto convertEntityToDto(ArticleEntity entity, Boolean favorited, Long favoritesCount, AuthUserDetails authUserDetails) {
    	ProfileDto author = profileService.getProfileByUserId(entity.getAuthor().getId(), authUserDetails);
    	return ArticleDto.builder()
    			.slug(entity.getSlug())
    			.title(entity.getTitle())
    			.description(entity.getDescription())
    			.body(entity.getBody())
    			.author(author)
    			.createdAt(entity.getCreatedAt())
    			.updatedAt(entity.getUpdatedAt())
    			.favorited(favorited)
    			.favoritesCount(favoritesCount)
    			.tagList(entity.getTagList().stream().map(ArticleTagRelationEntity::getTag).collect(Collectors.toList()))
    			.build();
    }
    
    private List<ArticleDto> convertToArticleList(List<ArticleEntity> articleEntities, AuthUserDetails authUserDetails) {
    	return articleEntities.stream().map(entity -> {
    		List<FavoriteEntity> favorites = entity.getFavoriteList();
    		Boolean favorited = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUserDetails.getId()));
    		int favoriteCount = favorites.size();
    		return convertEntityToDto(entity, favorited, (long) favoriteCount, authUserDetails);
    	}).collect(Collectors.toList());
    }
    
    @Transactional
	@Override
	public ArticleDto createArticle(ArticleDto article, AuthUserDetails authUserDetails) {
    	String slug = String.join("-", article.getTitle().split(" "));
    	UserEntity author = UserEntity.builder()
    			.id(authUserDetails.getId())
    			.build();
    	
    	ArticleEntity articleEntity = ArticleEntity.builder()
    			.slug(slug)
    			.title(article.getTitle())
    			.description(article.getDescription())
    			.body(article.getBody())
    			.author(author)
    			.build();
    	List<ArticleTagRelationEntity> tagList = new ArrayList<>();
    	for (String tag: article.getTagList()) {
    		tagList.add(ArticleTagRelationEntity.builder().article(articleEntity).tag(tag).build());
    	}
    	articleEntity.setTagList(tagList);
    	articleEntity = articleRepository.save(articleEntity);
    	return convertEntityToDto(articleEntity, false, 0L, authUserDetails);
	}

	@Override
	public ArticleDto getArticle(String slug, AuthUserDetails authUserDetails) {
		ArticleEntity found = articleRepository.findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
		List<FavoriteEntity> favorites = found.getFavoriteList();
		Boolean favorited = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUserDetails.getId()));
		int favoriteCount = favorites.size();
		return convertEntityToDto(found, favorited, (long) favoriteCount, authUserDetails);
	}

	@Transactional
	@Override
	public ArticleDto updateArticle(String slug, Update article, AuthUserDetails authUserDetails) {
		ArticleEntity found = articleRepository.findBySlug(slug)
				.filter(entity -> entity.getAuthor().getId()
						.equals(authUserDetails.getId()))
				.orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
		
        if (article.getTitle() != null) {
            String newSlug = String.join("-", article.getTitle().split(" "));
            found.setTitle(article.getTitle());
            found.setSlug(newSlug);
        }

        if (article.getDescription() != null) {
            found.setDescription(article.getDescription());
        }

        if (article.getBody() != null) {
            found.setBody(article.getBody());
        }

        articleRepository.save(found);

        return getArticle(slug, authUserDetails);
	}

	@Override
	public void deleteArticle(String slug, AuthUserDetails authUserDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug).filter(entity -> entity.getAuthor().getId().equals(authUserDetails.getId())).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        articleRepository.delete(found);
	}

	@Override
	public List<ArticleDto> feedArticles(AuthUserDetails authUserDetails, FeedParams feedParams) {
        List<Long> feedAuthorIds = followRepository.findByFollowerId(authUserDetails.getId()).stream().map(FollowEntity::getFollowee).map(BaseEntity::getId).collect(Collectors.toList());
        return articleRepository.findByAuthorIdInOrderByCreatedAtDesc(feedAuthorIds, PageRequest.of(feedParams.getOffset(), feedParams.getLimit())).stream().map(entity -> {
            List<FavoriteEntity> favorites = entity.getFavoriteList();
            Boolean favorited = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUserDetails.getId()));
            int favoriteCount = favorites.size();
            return convertEntityToDto(entity, favorited, (long) favoriteCount, authUserDetails);
        }).collect(Collectors.toList());
	}

	@Transactional
	@Override
	public ArticleDto favoriteArticle(String slug, AuthUserDetails authUserDetails) {
		ArticleEntity found = articleRepository.findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        favoriteRepository.findByArticleIdAndUserId(found.getId(), authUserDetails.getId())
                .ifPresent(favoriteEntity -> { throw new AppException(Error.ALREADY_FAVORITED_ARTICLE);});

        FavoriteEntity favorite = FavoriteEntity.builder()
                .article(found)
                .user(UserEntity.builder().id(authUserDetails.getId()).build())
                .build();
        favoriteRepository.save(favorite);

        return getArticle(slug, authUserDetails);
	}

	@Transactional
	@Override
	public ArticleDto unfavoriteArticle(String slug, AuthUserDetails authUserDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        FavoriteEntity favorite = found.getFavoriteList().stream()
                .filter(favoriteEntity -> favoriteEntity.getArticle().getId().equals(found.getId())
                        && favoriteEntity.getUser().getId().equals(authUserDetails.getId())).findAny()
                .orElseThrow(() -> new AppException(Error.FAVORITE_NOT_FOUND));
        found.getFavoriteList().remove(favorite); // cascade REMOVE
        return getArticle(slug, authUserDetails);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ArticleDto> listArticle(ArticleQueryParam articleQueryParam, AuthUserDetails authUserDetails) {
        Pageable pageable = null;
        if (articleQueryParam.getOffset() != null) {
            pageable = PageRequest.of(articleQueryParam.getOffset(), articleQueryParam.getLimit());
        }

        List<ArticleEntity> articleEntities;
        if (articleQueryParam.getTag() != null) {
            articleEntities = articleRepository.findByTag(articleQueryParam.getTag(), pageable);
        } else if  (articleQueryParam.getAuthor() != null) {
            articleEntities = articleRepository.findByAuthorName(articleQueryParam.getAuthor(), pageable);
        } else if (articleQueryParam.getFavorited() != null) {
            articleEntities = articleRepository.findByFavoritedUsername(articleQueryParam.getFavorited(), pageable);
        } else {
            articleEntities = articleRepository.findListByPaging(pageable);
        }

        return convertToArticleList(articleEntities, authUserDetails);
	}

}
