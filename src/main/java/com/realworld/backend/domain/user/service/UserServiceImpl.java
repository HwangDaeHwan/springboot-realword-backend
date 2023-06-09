package com.realworld.backend.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.realworld.backend.domain.user.dto.UserDto;
import com.realworld.backend.domain.user.entity.UserEntity;
import com.realworld.backend.domain.user.repository.UserRepository;
import com.realworld.backend.exception.AppException;
import com.realworld.backend.exception.Error;
import com.realworld.backend.security.AuthUserDetails;
import com.realworld.backend.security.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    
    private UserDto convertEntityToDto(UserEntity userEntity) {
    	return UserDto.builder()
    				.username(userEntity.getUsername())
    				.email(userEntity.getEmail())
    				.bio(userEntity.getBio())
    				.image(userEntity.getImage())
    				.token(jwtUtils.encode(userEntity.getEmail()))
    				.build();
    }
    
	@Override
	public UserDto registration(final UserDto.Registration registration) {
		userRepository.findByUsernameOrEmail(registration.getUsername(), registration.getEmail())
				.stream().findAny()
				.ifPresent(entity -> {
					throw new AppException(Error.DUPLICATED_USER);
				});
		UserEntity userEntity = UserEntity.builder()
				.username(registration.getUsername())
				.email(registration.getEmail())
				.password(passwordEncoder.encode(registration.getPassword()))
				.bio("")
				.build();
		userRepository.save(userEntity);
		return convertEntityToDto(userEntity);
	}

	@Transactional(readOnly = true)
	@Override
	public UserDto login(UserDto.Login login) {
		UserEntity userEntity = userRepository.findByEmail(login.getEmail())
				.filter(user -> passwordEncoder.matches(login.getPassword(), user.getPassword()))
				.orElseThrow(() -> new AppException(Error.LOGIN_INFO_INVALID));
		return convertEntityToDto(userEntity);
	}

	@Override
	public UserDto currentUser(AuthUserDetails authUserDetails) {
		UserEntity userEntity = userRepository.findById(authUserDetails.getId())
				.orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
		return convertEntityToDto(userEntity);
	}

	@Override
	public UserDto update(UserDto.Update update, AuthUserDetails authUserDetails) {
		UserEntity userEntity = userRepository.findById(authUserDetails.getId())
				.orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
		
		if (update.getUsername() != null) {
			userRepository.findByUsername(update.getUsername())
						.filter(found -> !found.getId().equals(userEntity.getId()))
						.ifPresent(found -> {throw new AppException(Error.DUPLICATED_USER);});
			userEntity.setUsername(update.getUsername());
		}
		
		if (update.getEmail() != null) {
			userRepository.findByEmail(update.getEmail())
						.filter(found -> !found.getId().equals(userEntity.getId()))
						.ifPresent(found -> {throw new AppException(Error.DUPLICATED_USER);});
			userEntity.setEmail(update.getEmail());
		}
		
        if (update.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(update.getPassword()));
        }

        if (update.getBio() != null) {
            userEntity.setBio(update.getBio());
        }

        if (update.getImage() != null) {
            userEntity.setImage(update.getImage());
        }

        userRepository.save(userEntity);
        return convertEntityToDto(userEntity);
	}

}
