package com.sunnyvet.microservice.service.impl;

import com.sunnyvet.microservice.domain.dto.TreatmentDto;
import com.sunnyvet.microservice.domain.entity.Treatment;
import com.sunnyvet.microservice.exception.ResourceNotFoundException;
import com.sunnyvet.microservice.repository.TreatmentRepository;
import com.sunnyvet.microservice.service.TreatmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TreatmentServiceImpl implements TreatmentService {

    private static final Logger logger = LoggerFactory.getLogger(TreatmentServiceImpl.class);
    private final TreatmentRepository treatmentRepository;

    public TreatmentServiceImpl(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    @Override
    public TreatmentDto createTreatment(TreatmentDto dto) {
        logger.info("Executing createTreatment functionality for pet ID: {}", dto.getPetId());

        Treatment treatment = new Treatment();
        treatment.setPetId(dto.getPetId());
        treatment.setDoctorId(dto.getDoctorId());
        treatment.setDescription(dto.getDescription());
        treatment.setCost(dto.getCost());

        Treatment savedTreatment = treatmentRepository.save(treatment);
        return mapToDto(savedTreatment);
    }

    @Override
    public TreatmentDto updateTreatment(UUID id, TreatmentDto dto) {
        logger.info("Executing updateTreatment functionality for treatment ID: {}", id);

        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment not found"));

        treatment.setDescription(dto.getDescription());
        treatment.setCost(dto.getCost());

        Treatment updatedTreatment = treatmentRepository.save(treatment);
        return mapToDto(updatedTreatment);
    }

    @Override
    public Map<String, Object> getTreatmentStats() {
        long totalTreatments = treatmentRepository.count();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTreatments", totalTreatments);
        return stats;
    }

    private TreatmentDto mapToDto(Treatment treatment) {
        TreatmentDto dto = new TreatmentDto();
        dto.setId(treatment.getId());
        dto.setPetId(treatment.getPetId());
        dto.setDoctorId(treatment.getDoctorId());
        dto.setDescription(treatment.getDescription());
        dto.setCost(treatment.getCost());
        return dto;
    }
}