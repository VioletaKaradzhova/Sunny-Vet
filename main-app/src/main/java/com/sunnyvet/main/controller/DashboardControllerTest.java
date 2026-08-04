package com.sunnyvet.main.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sunnyvet.main.domain.entity.UserEntity;
import com.sunnyvet.main.security.CustomUserDetailsService;
import com.sunnyvet.main.security.JwtAuthenticationFilter;
import com.sunnyvet.main.security.JwtService;
import com.sunnyvet.main.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(DashboardController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void testWelcome_WithoutToken_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/api/dashboard/welcome"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testWelcome_WithValidToken_ReturnsOk() throws Exception {
        String token = "valid-mock-token";
        String username = "admin";

        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(username);
        mockUser.setPassword("password");

        when(jwtService.extractUsername(token)).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(mockUser);
        when(jwtService.isTokenValid(token, username)).thenReturn(true);

        MvcResult result = mockMvc.perform(get("/api/dashboard/welcome")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertEquals("Welcome to the Sunny Vet protected dashboard!", responseBody);
    }
}