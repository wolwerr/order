package org.test.order.infra.dependecy.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class JwtRequestFilterTest {

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void test_valid_jwt_token_authenticates_and_continues_chain() throws ServletException, IOException {
        // Given
        JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
        JwtRequestFilter filter = new JwtRequestFilter(jwtTokenUtil);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid.jwt.token");
        when(jwtTokenUtil.validateToken("valid.jwt.token")).thenReturn(true);

        // When
        filter.doFilterInternal(request, response, chain);

        // Then
        verify(chain).doFilter(request, response);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("USER", auth.getPrincipal());
    }

    @Test
    public void test_missing_auth_header_continues_chain() throws ServletException, IOException {
        // Given
        JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
        JwtRequestFilter filter = new JwtRequestFilter(jwtTokenUtil);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        // When
        filter.doFilterInternal(request, response, chain);

        // Then
        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void test_empty_auth_header_continues_chain() throws ServletException, IOException {
        // Given
        JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
        JwtRequestFilter filter = new JwtRequestFilter(jwtTokenUtil);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("");

        // When
        filter.doFilterInternal(request, response, chain);

        // Then
        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void test_auth_header_without_bearer_continues_chain() throws ServletException, IOException {
        // Given
        JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
        JwtRequestFilter filter = new JwtRequestFilter(jwtTokenUtil);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("invalid.jwt.token");

        // When
        filter.doFilterInternal(request, response, chain);

        // Then
        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}