package com.realworld.backend.domain.tag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realworld.backend.domain.tag.dto.TagDto;
import com.realworld.backend.domain.tag.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
	private final TagService tagService;
	
	@GetMapping
	public TagDto.TagList listOfTags() {
		return TagDto.TagList.builder().tags(tagService.listOfTags()).build();
	}
}
