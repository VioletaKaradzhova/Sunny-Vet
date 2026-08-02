package com.sunnyvet.main.service;

import com.sunnyvet.main.domain.dto.AppointmentDto;
import java.util.UUID;

public interface AppointmentService {
    AppointmentDto createAppointment(AppointmentDto appointmentDto);
    AppointmentDto getAppointmentById(UUID id);
}