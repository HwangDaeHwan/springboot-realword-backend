package com.realworld.backend.domain.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.realworld.backend.domain.profile.dto.ProfileDto;
import com.realworld.backend.domain.profile.entity.FollowEntity;
import com.realworld.backend.domain.profile.repository.FollowRepository;
import com.realworld.backend.domain.user.entity.UserEntity;
import com.realworld.backend.domain.user.repository.UserRepository;
import com.realworld.backend.exception.AppException;
import com.realworld.backend.exception.Error;
import com.realworld.backend.security.AuthUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	
	private ProfileDto convertToProfile(UserEntity user, Boolean following) {
		return ProfileDto.builder()
					.username(user.getUsername())
					.bio(user.getBio())
					.image(user.getImage())
					.following(following)
					.build();
	}
	
	@Override
	public ProfileDto getProfile(String name, AuthUserDetails authUserDetails) {
		UserEntity user = userRepository.findByUsername(name)
				.orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
		Boolean following = followRepository.findByFolloweeIdAndFollwerId(user.getId(), 
				authUserDetails.getId()).isPresent();
		
		return convertToProfile(user, following);
	}

	@Transactional
	@Override
	public ProfileDto followUser(String name, AuthUserDetails authUserDetails) {
		UserEntity followee = userRepository.findByUsername(name).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
		UserEntity follower = UserEntity.builder().id(authUserDetails.getId()).build();
		
		followRepository.findByFolloweeIdAndFollwerId(followee.getId(), follower.getId())
					.ifPresent(follow -> {throw new AppException(Error.ALREADY_FOLLOWED_USER);});
		
		FollowEntity follow = FollowEntity.builder().followee(followee).follower(follower).build();
		followRepository.save(follow);
		
		return convertToProfile(followee, true);
	}

	@Transactional
	@Override
	public ProfileDto unfollowUser(String name, AuthUserDetails authUserDetails) {
		UserEntity followee = userRepository.findByUsername(name).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
		UserEntity follower = UserEntity.builder().id(authUserDetails.getId()).build();
		
		FollowEntity follow = followRepository.findByFolloweeIdAndFollwerId(followee.getId(), follower.getId())
				.orElseThrow(() ->  new AppException(Error.FOLLOW_NOT_FOUND));
		followRepository.delete(follow);
		
		return convertToProfile(follower, false);
	}

	@Override
	public ProfileDto getProfileByUserId(Long userId, AuthUserDetails authUserDetails) {
		UserEntity user = userRepository.findById(userId).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
		Boolean following = followRepository.findByFolloweeIdAndFollwerId(user.getId(), authUserDetails.getId()).isPresent();
		
		return convertToProfile(user, following);
	}

}
