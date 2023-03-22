package com.realworld.backend.security;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationProvider {
	private final UserDetailsService userDetailsService;
	
	public Authentication getAuthentication(String username) {
		return Optional.ofNullable(username)
				.map(userDetailsService::loadUserByUsername)
				.map(userDetails -> 
						new UsernamePasswordAuthenticationToken(userDetails, 
								userDetails.getPassword(), 
								userDetails.getAuthorities()))
				.orElse(null);
	}
}
