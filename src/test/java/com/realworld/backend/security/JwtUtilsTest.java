package com.realworld.backend.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class JwtUtilsTest {
	JwtUtils jwtUtils;
	
	@BeforeEach
	void setUp() {
		jwtUtils = new JwtUtils("signKey012345678901234567890123456789", 3000L);
	}
	
	@ParameterizedTest
	@MethodSource("subs")
	void whenSubIsNotNull_thenRetrunJwt_orNull(String sub) {
		String actual = jwtUtils.encode(sub);
		if (sub == null || sub.equals("")) {
			assertNull(actual);
		} else {
			assertNotNull(actual);
		}
	}
	
	@Test
	void whenValidJwt_thenReturnTrue() {
		String jwt = jwtUtils.encode("sub1");
		boolean actual = jwtUtils.validateToken(jwt);
		assertTrue(actual);
	}
	
    @Test
    void whenInvalidJWT_thenReturnFalse() {
        String jwt = jwtUtils.encode("sub1");
        Instant now = Instant.now().plusSeconds(100000L);

        try (MockedStatic<Instant> mockInstant = Mockito.mockStatic(Instant.class)) {
            mockInstant.when(Instant::now).thenReturn(now);

            boolean actual = jwtUtils.validateToken(jwt);

            assertFalse(actual);
        }
    }


    public static Stream<String> subs() {
        return Stream.of("sub1", "sub2", "", null);
    }
}
