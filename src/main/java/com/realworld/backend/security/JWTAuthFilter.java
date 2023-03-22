package com.realworld.backend.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends GenericFilter {
	private static final long serialVersionUID = -6403570323103945538L;

	public static final String TOKEN_PREFIX = "Token ";
	private final JwtUtils jwtUtils;
	private final AuthenticationProvider authenticationProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Optional.ofNullable(((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION))
				.filter(authHeader -> authHeader.startsWith(TOKEN_PREFIX))
				.map(authHeader -> authHeader.substring(TOKEN_PREFIX.length())).filter(jwtUtils::validateToken)
				.map(jwtUtils::getSub).map(authenticationProvider::getAuthentication)
				.ifPresent(SecurityContextHolder.getContext()::setAuthentication);
		chain.doFilter(request, response);
	}

}
