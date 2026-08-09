package com.sunnyvet.main.service.impl;

import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.domain.entity.Appointment;
import com.sunnyvet.main.domain.entity.Doctor;
import com.sunnyvet.main.domain.entity.Pet;
import com.sunnyvet.main.exception.ResourceNotFoundException;
import com.sunnyvet.main.repository.AppointmentRepository;
import com.sunnyvet.main.service.AppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentDto createAppointment(AppointmentDto dto) {
        Appointment appointment = new Appointment();

        if (dto.getPetId() != null) {
            Pet pet = new Pet();
            pet.setId(dto.getPetId());
            appointment.setPet(pet);
        }
        if (dto.getDoctorId() != null) {
            Doctor doctor = new Doctor();
            doctor.setId(dto.getDoctorId());
            appointment.setDoctor(doctor);
        }

        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setReason(dto.getReason());

        Appointment saved = appointmentRepository.save(appointment);
        return mapToDto(saved);
    }

    @Override
    public AppointmentDto getAppointmentById(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
        return mapToDto(appointment);
    }

    @Override
    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDto updateAppointment(UUID id, AppointmentDto dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        if (dto.getPetId() != null) {
            Pet pet = new Pet();
            pet.setId(dto.getPetId());
            appointment.setPet(pet);
        }
        if (dto.getDoctorId() != null) {
            Doctor doctor = new Doctor();
            doctor.setId(dto.getDoctorId());
            appointment.setDoctor(doctor);
        }

        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setReason(dto.getReason());

        Appointment updated = appointmentRepository.save(appointment);
        return mapToDto(updated);
    }

    @Override
    public void deleteAppointment(UUID id) {
        appointmentRepository.deleteById(id);
    }

    private AppointmentDto mapToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());

        if (appointment.getPet() != null) {
            dto.setPetId(appointment.getPet().getId());
            dto.setPetName(appointment.getPet().getName());
        }
        if (appointment.getDoctor() != null) {
            dto.setDoctorId(appointment.getDoctor().getId());
            dto.setDoctorName(appointment.getDoctor().getFullName());
        }

        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setReason(appointment.getReason());
        return dto;
    }
}