package com.realworld.backend.domain.tag.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.realworld.backend.domain.tag.entity.ArticleTagRelationEntity;
import com.realworld.backend.domain.tag.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
	private final TagRepository tagRepository;
	
	@Override
	public List<String> listOfTags() {
		return tagRepository.findAll()
					.stream()
					.map(ArticleTagRelationEntity::getTag)
					.distinct()
					.collect(Collectors.toList());
	}
}
