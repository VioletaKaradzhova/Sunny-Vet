package com.sunnyvet.microservice.domain;

import com.sunnyvet.microservice.domain.dto.TreatmentDto;
import com.sunnyvet.microservice.domain.entity.Treatment;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MicroserviceDomainCoverageTest {

    @Test
    void testTreatmentEntityAndDto() {
        UUID id = UUID.randomUUID();
        UUID petId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Treatment treatment = new Treatment();
        treatment.setId(id);
        treatment.setPetId(petId);
        treatment.setDescription("Vaccine");
        treatment.setMedication("None");
        treatment.setTreatmentDate(now);

        assertEquals(id, treatment.getId());
        assertEquals(petId, treatment.getPetId());
        assertEquals("Vaccine", treatment.getDescription());
        assertEquals("None", treatment.getMedication());
        assertEquals(now, treatment.getTreatmentDate());

        TreatmentDto dto = new TreatmentDto();
        dto.setId(id);
        dto.setPetId(petId);
        dto.setDescription("Vaccine");
        dto.setMedication("None");
        dto.setTreatmentDate(now);

        assertEquals(id, dto.getId());
        assertEquals(petId, dto.getPetId());
        assertEquals("Vaccine", dto.getDescription());
    }
}