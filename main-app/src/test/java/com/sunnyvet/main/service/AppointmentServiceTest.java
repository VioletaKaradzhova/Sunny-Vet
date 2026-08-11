package com.sunnyvet.main.service;

import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.domain.entity.Appointment;
import com.sunnyvet.main.repository.AppointmentRepository;
import com.sunnyvet.main.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void getAppointmentById_WhenExists_ReturnsDto() {
        UUID id = UUID.randomUUID();
        Appointment appointment = new Appointment();
        appointment.setId(id);

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));

        AppointmentDto result = appointmentService.getAppointmentById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getAppointmentById_WhenDoesNotExist_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> appointmentService.getAppointmentById(id));
    }

    @Test
    void createAppointment_ValidDto_SavesAndReturnsDto() {
        AppointmentDto input = new AppointmentDto();
        Appointment savedEntity = new Appointment();
        savedEntity.setId(UUID.randomUUID());

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedEntity);

        appointmentService.createAppointment(input);

        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }
}