package com.sunnyvet.main.repository;

import com.sunnyvet.main.domain.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByDoctorId(UUID doctorId);
    List<Appointment> findByPetId(UUID petId);
}