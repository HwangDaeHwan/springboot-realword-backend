package com.realworld.backend.domain.article.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.realworld.backend.domain.article.dto.CommentDto;
import com.realworld.backend.domain.article.entity.ArticleEntity;
import com.realworld.backend.domain.article.entity.CommentEntity;
import com.realworld.backend.domain.article.repository.ArticleRepository;
import com.realworld.backend.domain.article.repository.CommentRepository;
import com.realworld.backend.domain.common.entity.BaseEntity;
import com.realworld.backend.domain.profile.dto.ProfileDto;
import com.realworld.backend.domain.profile.service.ProfileService;
import com.realworld.backend.domain.user.entity.UserEntity;
import com.realworld.backend.exception.AppException;
import com.realworld.backend.exception.Error;
import com.realworld.backend.security.AuthUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final ArticleRepository articleRepository;
	private final CommentRepository commentRepository;
	private final ProfileService profileService;
	
	private CommentDto convertToDTO(AuthUserDetails authUserDetails, CommentEntity commentEntity) {
		ProfileDto author = profileService.getProfileByUserId(commentEntity.getAuthor().getId(), authUserDetails);
		return CommentDto.builder()
				.id(commentEntity.getId())
				.createdAt(commentEntity.getCreatedAt())
				.updatedAt(commentEntity.getUpdatedAt())
				.body(commentEntity.getBody())
				.author(author)
				.build();
	}
	
	@Transactional
	@Override
	public CommentDto addCommentsToAnArticle(String slug, CommentDto comment, AuthUserDetails authUserDetails) {
		ArticleEntity articleEntity = articleRepository.findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
		CommentEntity commentEntity = CommentEntity.builder()
				.body(comment.getBody())
				.author(UserEntity.builder().id(authUserDetails.getId()).build())
				.article(articleEntity)
				.build();
		commentRepository.save(commentEntity);
		
		return convertToDTO(authUserDetails, commentEntity);
	}

	@Transactional
	@Override
	public void delete(String slug, Long commentId, AuthUserDetails authUserDetails) {
		Long articleId = articleRepository.findBySlug(slug).map(BaseEntity::getId).orElseThrow(
				() -> new AppException(Error.ARTICLE_NOT_FOUND));
		CommentEntity commentEntity = commentRepository.findById(commentId)
				.filter(comment -> comment.getArticle().getId().equals(articleId))
				.orElseThrow(() -> new AppException(Error.COMMENT_NOT_FOUND));
		
		commentRepository.delete(commentEntity);
	}

	@Override
	public List<CommentDto> getCommentsBySlug(String slug, AuthUserDetails authUserDetails) {
		 Long articleId = articleRepository.findBySlug(slug).map(BaseEntity::getId).orElseThrow(() -> 
	        new AppException(Error.ARTICLE_NOT_FOUND));
		 
		 List<CommentEntity> commentEntities = commentRepository.findByArticleId(articleId);
		return commentEntities.stream().map(CommentEntity -> convertToDTO(authUserDetails, CommentEntity)).collect(Collectors.toList());
	}

}
