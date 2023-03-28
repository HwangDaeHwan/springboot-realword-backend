package com.realworld.backend.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class JwtUtilsConfigurationTest {

	JwtUtilsConfiguration jwtUtilsConfiguration;
	
	@Test
	void whenSignKeyShorterThan32_thenThrow() {
		jwtUtilsConfiguration = new JwtUtilsConfiguration();
		
		String signKey = "shortSignKey";
		
		try {
			jwtUtilsConfiguration.getJwtUtils(signKey, 3000L);
			fail();
		} catch (Exception e) {
			assertEquals(e.getMessage(), "signKey must have length at least 32");
		}
	}
}
