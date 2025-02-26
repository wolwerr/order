package org.test.order.infra.dependecy.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.test.util.ReflectionTestUtils;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", "mySecretKey");
    }

    @Test
    public void test_generate_valid_jwt_token() {
        // Given
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String secretKey = Arrays.toString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenUtil, "expirationTimeInMinutes", 30L);

        // When
        String token = jwtTokenUtil.generateToken();

        // Then
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo("admin");
        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiration()).isAfter(new Date());
    }

    // Validate a correctly formatted and signed JWT token
    @Test
    public void test_validate_correct_jwt_token() {
        // Given
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String secretKey = Arrays.toString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenUtil, "expirationTimeInMinutes", 30L);
        String token = jwtTokenUtil.generateToken();

        // When
        boolean isValid = jwtTokenUtil.validateToken(token);

        // Then
        assertThat(isValid).isTrue();
    }

    // Handle negative or zero expiration time configuration
    @Test
    public void test_zero_expiration_time_throws_exception() {
        // Given
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String secretKey = Arrays.toString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenUtil, "expirationTimeInMinutes", 0L);

        // When/Then
        assertThrows(IllegalArgumentException.class, jwtTokenUtil::generateToken);
    }

@Test
void test_validateAuthorizationHeader_withNullHeader() {
    // Given
    String authorizationHeader = null;

    // When/Then
    SecurityException exception = assertThrows(SecurityException.class, () -> {
        jwtTokenUtil.validateAuthorizationHeader(null);
    });
    assertEquals("Invalid Authorization header", exception.getMessage());
}

    @Test
    void test_validateAuthorizationHeader_withValidHeader() {
        // Given
        String authorizationHeader = "Bearer mySecretKey";

        // When
        boolean isValid = jwtTokenUtil.validateAuthorizationHeader(authorizationHeader);

        // Then
        assertTrue(isValid);
    }

    @Test
    void test_validateAuthorizationHeader_withInvalidHeader() {
        // Given
        String authorizationHeader = "Bearer invalidKey";

        // When
        boolean isValid = jwtTokenUtil.validateAuthorizationHeader(authorizationHeader);

        // Then
        assertFalse(isValid);
    }
}