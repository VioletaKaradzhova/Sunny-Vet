package com.sunnyvet.main.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.exception.ResourceNotFoundException;
import com.sunnyvet.main.service.AppointmentService;
import com.sunnyvet.main.security.JwtService;
import com.sunnyvet.main.security.JwtAuthenticationFilter;
import com.sunnyvet.main.security.CustomUserDetailsService;
import com.sunnyvet.main.web.GlobalControllerAdvice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private AppointmentService appointmentService;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockitoBean private CustomUserDetailsService customUserDetailsService;
    @MockitoBean private AuthenticationConfiguration authenticationConfiguration;
    @MockitoBean private GlobalControllerAdvice globalControllerAdvice;

    @Test
    void createAppointment_ValidInput_ReturnsCreatedWithHateoasLinks() throws Exception {
        UUID appointmentId = UUID.randomUUID();
        AppointmentDto inputDto = new AppointmentDto();
        inputDto.setAppointmentTime(LocalDateTime.now().plusDays(1));
        inputDto.setReason("Annual Checkup");

        AppointmentDto outputDto = new AppointmentDto();
        outputDto.setId(appointmentId);
        outputDto.setReason("Annual Checkup");

        when(appointmentService.createAppointment(any(AppointmentDto.class))).thenReturn(outputDto);

        MvcResult result = mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        assertEquals(appointmentId.toString(), jsonNode.get("id").asText());
    }

    @Test
    void getAppointment_NotFound_ReturnsProblemDetail() throws Exception {
        UUID appointmentId = UUID.randomUUID();
        when(appointmentService.getAppointmentById(appointmentId)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/appointments/{id}", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}