package com.sunnyvet.microservice.repository;

import com.sunnyvet.microservice.domain.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, UUID> {
    List<Treatment> findByPetIdOrderByTreatmentDateDesc(UUID petId);
}