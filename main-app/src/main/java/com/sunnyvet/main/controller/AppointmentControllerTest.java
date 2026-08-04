package com.sunnyvet.main.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.exception.ResourceNotFoundException;
import com.sunnyvet.main.service.AppointmentService;
import com.sunnyvet.main.security.JwtService;
import com.sunnyvet.main.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppointmentService appointmentService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void createAppointment_ValidInput_ReturnsCreatedWithHateoasLinks() throws Exception {
        UUID appointmentId = UUID.randomUUID();
        UUID petId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();

        AppointmentDto inputDto = new AppointmentDto();
        inputDto.setPetId(petId);
        inputDto.setDoctorId(doctorId);
        inputDto.setAppointmentTime(LocalDateTime.now().plusDays(1));
        inputDto.setReason("Annual Checkup");

        AppointmentDto outputDto = new AppointmentDto();
        outputDto.setId(appointmentId);
        outputDto.setPetId(petId);
        outputDto.setDoctorId(doctorId);
        outputDto.setAppointmentTime(inputDto.getAppointmentTime());
        outputDto.setReason("Annual Checkup");

        when(appointmentService.createAppointment(any(AppointmentDto.class))).thenReturn(outputDto);

        MvcResult result = mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        assertEquals(appointmentId.toString(), jsonNode.get("id").asText());
        assertEquals("Annual Checkup", jsonNode.get("reason").asText());
        assertNotNull(jsonNode.get("_links").get("self").get("href").asText());
    }

    @Test
    void getAppointment_NotFound_ReturnsProblemDetail() throws Exception {
        UUID appointmentId = UUID.randomUUID();
        when(appointmentService.getAppointmentById(appointmentId)).thenThrow(new ResourceNotFoundException("Appointment not found"));

        MvcResult result = mockMvc.perform(get("/api/appointments/{id}", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        assertEquals("https://api.sunnyvet.com/errors/not-found", jsonNode.get("type").asText());
        assertEquals("Resource Not Found", jsonNode.get("title").asText());
        assertNotNull(jsonNode.get("detail").asText());
        assertNotNull(jsonNode.get("timestamp").asText());
    }

    @Test
    void createAppointment_InvalidInput_ReturnsValidationProblemDetail() throws Exception {
        AppointmentDto invalidDto = new AppointmentDto();

        MvcResult result = mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        assertEquals("https://api.sunnyvet.com/errors/validation-failed", jsonNode.get("type").asText());
        assertEquals("Validation Error", jsonNode.get("title").asText());
        assertNotNull(jsonNode.get("invalid_fields"));
    }
}