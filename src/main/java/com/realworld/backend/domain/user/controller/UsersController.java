package com.realworld.backend.domain.user.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realworld.backend.domain.user.dto.UserDto;
import com.realworld.backend.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
	private final UserService userService;
	
	@PostMapping
	public UserDto registration(@RequestBody @Valid UserDto.Registration registration) {
		return userService.registration(registration);
	}
	
	@PostMapping("/login")
	public UserDto login(@RequestBody @Valid UserDto.Login login) {
		return userService.login(login);
	}
}
