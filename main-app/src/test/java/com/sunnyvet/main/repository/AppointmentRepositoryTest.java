package com.sunnyvet.main.repository;

import com.sunnyvet.main.domain.entity.Appointment;
import com.sunnyvet.main.domain.entity.Doctor;
import com.sunnyvet.main.domain.entity.Owner;
import com.sunnyvet.main.domain.entity.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void findByDoctorId_ReturnsAppointments() {
        Doctor doctor = new Doctor();
        doctor.setFullName("Dr. Sunny");
        doctor.setSpecialization("Surgery");
        entityManager.persistAndFlush(doctor);

        Owner owner = new Owner();
        owner.setFullName("Jane Doe");
        owner.setPhoneNumber("555-1234");
        entityManager.persistAndFlush(owner);

        Pet pet = new Pet();
        pet.setName("Buddy");
        pet.setSpecies("Dog");
        pet.setOwner(owner);
        entityManager.persistAndFlush(pet);

        Appointment appointment = new Appointment();
        appointment.setAppointmentTime(LocalDateTime.now().plusDays(1));
        appointment.setReason("Checkup");
        appointment.setDoctor(doctor);
        appointment.setPet(pet);
        entityManager.persistAndFlush(appointment);

        List<Appointment> results = appointmentRepository.findByDoctorId(doctor.getId());

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("Checkup", results.get(0).getReason());
    }

    @Test
    void findByPetId_ReturnsAppointments() {
        Doctor doctor = new Doctor();
        doctor.setFullName("Dr. Smith");
        doctor.setSpecialization("General Practice");
        entityManager.persistAndFlush(doctor);

        Owner owner = new Owner();
        owner.setFullName("John Doe");
        owner.setPhoneNumber("555-9876");
        entityManager.persistAndFlush(owner);

        Pet pet = new Pet();
        pet.setName("Luna");
        pet.setSpecies("Cat");
        pet.setOwner(owner);
        entityManager.persistAndFlush(pet);

        Appointment appointment = new Appointment();
        appointment.setAppointmentTime(LocalDateTime.now().plusDays(2));
        appointment.setReason("Vaccination");
        appointment.setDoctor(doctor);
        appointment.setPet(pet);
        entityManager.persistAndFlush(appointment);

        List<Appointment> results = appointmentRepository.findByPetId(pet.getId());

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("Vaccination", results.get(0).getReason());
    }
}