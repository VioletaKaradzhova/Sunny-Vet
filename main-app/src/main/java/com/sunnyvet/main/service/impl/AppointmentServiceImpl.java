package com.sunnyvet.main.service.impl;

import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.domain.entity.Appointment;
import com.sunnyvet.main.domain.entity.Doctor;
import com.sunnyvet.main.domain.entity.Pet;
import com.sunnyvet.main.event.AppointmentCreatedEvent;
import com.sunnyvet.main.exception.ResourceNotFoundException;
import com.sunnyvet.main.repository.AppointmentRepository;
import com.sunnyvet.main.repository.DoctorRepository;
import com.sunnyvet.main.repository.PetRepository;
import com.sunnyvet.main.service.AppointmentService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final DoctorRepository doctorRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ApplicationEventPublisher eventPublisher;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  PetRepository petRepository,
                                  DoctorRepository doctorRepository,
                                  KafkaTemplate<String, String> kafkaTemplate,
                                  ApplicationEventPublisher eventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.petRepository = petRepository;
        this.doctorRepository = doctorRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
        Pet pet = petRepository.findById(appointmentDto.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        Doctor doctor = doctorRepository.findById(appointmentDto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Appointment appointment = new Appointment();
        appointment.setAppointmentTime(appointmentDto.getAppointmentTime());
        appointment.setReason(appointmentDto.getReason());
        appointment.setPet(pet);
        appointment.setDoctor(doctor);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        kafkaTemplate.send("appointments-topic", "New appointment booked: " + savedAppointment.getId());
        eventPublisher.publishEvent(new AppointmentCreatedEvent(this, savedAppointment.getId()));

        return mapToDto(savedAppointment);
    }

    @Override
    public AppointmentDto getAppointmentById(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        return mapToDto(appointment);
    }

    private AppointmentDto mapToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setReason(appointment.getReason());
        dto.setPetId(appointment.getPet().getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        return dto;
    }
}