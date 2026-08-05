package com.sunnyvet.main.service;

import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.domain.entity.Appointment;
import com.sunnyvet.main.domain.entity.Doctor;
import com.sunnyvet.main.domain.entity.Pet;
import com.sunnyvet.main.event.AppointmentCreatedEvent;
import com.sunnyvet.main.exception.ResourceNotFoundException;
import com.sunnyvet.main.repository.AppointmentRepository;
import com.sunnyvet.main.repository.DoctorRepository;
import com.sunnyvet.main.repository.PetRepository;
import com.sunnyvet.main.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void getAppointmentById_WhenExists_ReturnsDto() {
        UUID appointmentId = UUID.randomUUID();
        UUID petId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        LocalDateTime time = LocalDateTime.now().plusDays(1);

        Pet mockPet = new Pet();
        mockPet.setId(petId);

        Doctor mockDoctor = new Doctor();
        mockDoctor.setId(doctorId);

        Appointment mockAppointment = new Appointment();
        mockAppointment.setId(appointmentId);
        mockAppointment.setReason("Checkup");
        mockAppointment.setAppointmentTime(time);
        mockAppointment.setPet(mockPet);
        mockAppointment.setDoctor(mockDoctor);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(mockAppointment));

        AppointmentDto result = appointmentService.getAppointmentById(appointmentId);

        assertNotNull(result);
        assertEquals(appointmentId, result.getId());
        assertEquals("Checkup", result.getReason());
        assertEquals(petId, result.getPetId());
        assertEquals(doctorId, result.getDoctorId());
        assertEquals(time, result.getAppointmentTime());

        verify(appointmentRepository, times(1)).findById(appointmentId);
    }

    @Test
    void getAppointmentById_WhenDoesNotExist_ThrowsException() {
        UUID appointmentId = UUID.randomUUID();
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.getAppointmentById(appointmentId));

        verify(appointmentRepository, times(1)).findById(appointmentId);
    }

    @Test
    void createAppointment_ValidDto_SavesAndReturnsDto() {
        UUID newId = UUID.randomUUID();
        UUID petId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        LocalDateTime time = LocalDateTime.now().plusDays(2);

        AppointmentDto incomingDto = new AppointmentDto();
        incomingDto.setReason("Vaccination");
        incomingDto.setPetId(petId);
        incomingDto.setDoctorId(doctorId);
        incomingDto.setAppointmentTime(time);

        Pet mockPet = new Pet();
        mockPet.setId(petId);

        Doctor mockDoctor = new Doctor();
        mockDoctor.setId(doctorId);

        Appointment savedEntity = new Appointment();
        savedEntity.setId(newId);
        savedEntity.setReason("Vaccination");
        savedEntity.setAppointmentTime(time);
        savedEntity.setPet(mockPet);
        savedEntity.setDoctor(mockDoctor);

        when(petRepository.findById(petId)).thenReturn(Optional.of(mockPet));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(mockDoctor));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedEntity);

        AppointmentDto result = appointmentService.createAppointment(incomingDto);

        assertNotNull(result);
        assertEquals(newId, result.getId());
        assertEquals("Vaccination", result.getReason());
        assertEquals(petId, result.getPetId());
        assertEquals(doctorId, result.getDoctorId());
        assertEquals(time, result.getAppointmentTime());

        verify(petRepository, times(1)).findById(petId);
        verify(doctorRepository, times(1)).findById(doctorId);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
        verify(kafkaTemplate, times(1)).send(eq("appointments-topic"), anyString());
        verify(eventPublisher, times(1)).publishEvent(any(AppointmentCreatedEvent.class));
    }
}