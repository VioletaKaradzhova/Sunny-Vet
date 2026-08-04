package com.sunnyvet.main.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;
    private String username;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        try {
            ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
            ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);
        } catch (IllegalArgumentException e) {
        }

        username = "dr.smith@sunnyvet.com";
        userDetails = new User(username, "password", new ArrayList<>());
    }

    @Test
    void generateToken_ReturnsValidToken() {
        String token = jwtService.generateToken(username);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_ReturnsCorrectUsername() {
        String token = jwtService.generateToken(username);
        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void isTokenValid_WithCorrectUser_ReturnsTrue() {
        String token = jwtService.generateToken(username);

        assertTrue(jwtService.isTokenValid(token, username));
    }

    @Test
    void isTokenValid_WithIncorrectUser_ReturnsFalse() {
        String token = jwtService.generateToken(username);

        assertFalse(jwtService.isTokenValid(token, "other.user@sunnyvet.com"));
    }
}