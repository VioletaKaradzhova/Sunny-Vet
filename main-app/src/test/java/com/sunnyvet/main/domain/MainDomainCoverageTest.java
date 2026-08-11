package com.sunnyvet.main.domain;

import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.domain.dto.DoctorFormDto;
import com.sunnyvet.main.domain.dto.PetDto;
import com.sunnyvet.main.domain.entity.Appointment;
import com.sunnyvet.main.domain.entity.Doctor;
import com.sunnyvet.main.domain.entity.Pet;
import com.sunnyvet.main.domain.entity.UserEntity;
import com.sunnyvet.main.domain.enums.Role;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainDomainCoverageTest {

    @Test
    void testEntityAndDtoGettersSetters() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Appointment appt = new Appointment();
        appt.setId(id);
        appt.setReason("Checkup");
        appt.setAppointmentTime(now);
        assertEquals(id, appt.getId());
        assertEquals("Checkup", appt.getReason());
        assertEquals(now, appt.getAppointmentTime());

        AppointmentDto apptDto = new AppointmentDto();
        apptDto.setId(id);
        apptDto.setReason("Checkup");
        apptDto.setDoctorId(id);
        apptDto.setPetId(id);
        assertEquals(id, apptDto.getId());

        Pet pet = new Pet();
        pet.setId(id);
        pet.setName("Rex");
        pet.setSpecies("Dog");
        pet.setAge(5);
        assertEquals("Rex", pet.getName());
        assertEquals("Dog", pet.getSpecies());
        assertEquals(5, pet.getAge());

        PetDto petDto = new PetDto();
        petDto.setId(id);
        petDto.setName("Rex");
        petDto.setOwnerId(id);
        assertEquals("Rex", petDto.getName());

        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername("admin");
        user.setRole(Role.ADMIN);
        assertEquals("admin", user.getUsername());
        assertEquals(Role.ADMIN, user.getRole());

        Doctor doc = new Doctor();
        doc.setId(id);
        doc.setFullName("Dr. Smith");
        doc.setSpecialization("Surgery");
        assertEquals("Dr. Smith", doc.getFullName());

        DoctorFormDto docDto = new DoctorFormDto();
        docDto.setId(id);
        docDto.setUsername("doc");
        docDto.setEmail("doc@test.com");
        assertEquals("doc", docDto.getUsername());
    }
}