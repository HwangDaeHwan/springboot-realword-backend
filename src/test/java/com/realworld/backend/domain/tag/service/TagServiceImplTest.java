package com.realworld.backend.domain.tag.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.realworld.backend.domain.tag.entity.ArticleTagRelationEntity;
import com.realworld.backend.domain.tag.repository.TagRepository;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {
    TagServiceImpl tagService;

    @Mock
    TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagRepository);
    }

    @Test
    void whenThereAreTags_thenReturnAllTags() {
        when(tagRepository.findAll()).thenReturn(List.of(ArticleTagRelationEntity.builder().tag("a").build()));

        List<String> actual = tagService.listOfTags();

        assertTrue(actual.size() > 0);
    }
}
