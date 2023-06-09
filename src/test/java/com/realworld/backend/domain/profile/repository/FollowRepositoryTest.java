package com.realworld.backend.domain.profile.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.realworld.backend.domain.profile.entity.FollowEntity;
import com.realworld.backend.domain.user.entity.UserEntity;
import com.realworld.backend.domain.user.repository.UserRepository;

@DataJpaTest
public class FollowRepositoryTest {
    @Autowired
    FollowRepository followRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void whenAlreadySavedFolloweeAndFollowerPair_thenThrow() {
        UserEntity follower = UserEntity.builder()
                .username("follower")
                .password("password123")
                .email("follower@email.com")
                .bio("expected bio")
                .image("expected_image_path")
                .build();

        UserEntity followee = UserEntity.builder()
                .username("followee")
                .password("password123")
                .email("followee@email.com")
                .bio("expected bio")
                .image("expected_image_path")
                .build();

        userRepository.saveAll(List.of(followee, follower));

        FollowEntity follow = FollowEntity.builder().follower(follower).followee(followee).build();
        followRepository.save(follow);

        assertThrows(Exception.class, () -> {
            FollowEntity duplicatedFollow = FollowEntity.builder().follower(follower).followee(followee).build();
            followRepository.save(duplicatedFollow);
        });
    }
}
