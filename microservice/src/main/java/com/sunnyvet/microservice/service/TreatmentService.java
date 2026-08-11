package com.sunnyvet.microservice.service;

import com.sunnyvet.microservice.domain.dto.TreatmentDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TreatmentService {
    List<TreatmentDto> getTreatmentsByPetId(UUID petId);
    TreatmentDto createTreatment(TreatmentDto dto);
    TreatmentDto updateTreatment(UUID id, TreatmentDto dto);
    Map<String, Object> getTreatmentStats();
}