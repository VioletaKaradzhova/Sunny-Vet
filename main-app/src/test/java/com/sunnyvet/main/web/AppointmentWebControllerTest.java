package com.sunnyvet.main.web;

import com.sunnyvet.main.config.UiSecurityConfig;
import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.security.JwtAuthenticationFilter;
import com.sunnyvet.main.security.SecurityConfig;
import com.sunnyvet.main.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(
        value = AppointmentWebController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {UiSecurityConfig.class, SecurityConfig.class, JwtAuthenticationFilter.class}
        )
)
public class AppointmentWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Test
    public void testListAppointments() throws Exception {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(UUID.randomUUID());
        dto.setPetId(UUID.randomUUID());
        dto.setDoctorId(UUID.randomUUID());
        dto.setAppointmentTime(LocalDateTime.now().plusDays(1));
        dto.setReason("Checkup");

        when(appointmentService.getAllAppointments()).thenReturn(List.of(dto));

        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(view().name("appointments/list"))
                .andExpect(model().attributeExists("appointments"));
    }

    @Test
    public void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/appointments/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("appointments/new"))
                .andExpect(model().attributeExists("appointment"));
    }

    @Test
    public void testCreateAppointmentSuccess() throws Exception {
        String futureTime = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        mockMvc.perform(post("/appointments")
                        .param("petId", UUID.randomUUID().toString())
                        .param("doctorId", UUID.randomUUID().toString())
                        .param("appointmentTime", futureTime)
                        .param("reason", "Checkup"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointments"));
    }

    @Test
    public void testCreateAppointmentValidationErrors() throws Exception {
        String pastTime = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        mockMvc.perform(post("/appointments")
                        .param("reason", "")
                        .param("appointmentTime", pastTime))
                .andExpect(status().isOk())
                .andExpect(view().name("appointments/new"))
                .andExpect(model().attributeHasFieldErrors("appointment", "petId", "doctorId", "appointmentTime", "reason"));
    }
}