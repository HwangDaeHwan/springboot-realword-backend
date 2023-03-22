package com.realworld.backend.domain.user.service;

import com.realworld.backend.domain.user.dto.UserDto;
import com.realworld.backend.security.AuthUserDetails;

public interface UserService {
    UserDto registration(final UserDto.Registration registration);

    UserDto login(final UserDto.Login login);

    UserDto currentUser(final AuthUserDetails authUserDetails);

    UserDto update(final UserDto.Update update, final AuthUserDetails authUserDetails);
}