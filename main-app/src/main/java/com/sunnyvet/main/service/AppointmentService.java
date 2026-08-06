package com.sunnyvet.main.service;

import com.sunnyvet.main.domain.dto.AppointmentDto;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    AppointmentDto createAppointment(AppointmentDto dto);
    AppointmentDto getAppointmentById(UUID id);
    List<AppointmentDto> getAllAppointments();
    AppointmentDto updateAppointment(UUID id, AppointmentDto dto);
    void deleteAppointment(UUID id);
}