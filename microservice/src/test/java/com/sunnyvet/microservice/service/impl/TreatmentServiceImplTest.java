package com.sunnyvet.microservice.service.impl;

import com.sunnyvet.microservice.domain.dto.TreatmentDto;
import com.sunnyvet.microservice.domain.entity.Treatment;
import com.sunnyvet.microservice.repository.TreatmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TreatmentServiceImplTest {

    @Mock
    private TreatmentRepository treatmentRepository;

    @InjectMocks
    private TreatmentServiceImpl treatmentService;

    @Test
    void testCreateTreatment_ShouldReturnDto() {

        UUID petId = UUID.randomUUID();
        UUID treatmentId = UUID.randomUUID();

        TreatmentDto inputDto = new TreatmentDto();
        inputDto.setPetId(petId);
        inputDto.setDescription("Checkup");

        Treatment savedEntity = new Treatment();
        savedEntity.setId(treatmentId);
        savedEntity.setPetId(petId);
        savedEntity.setDescription("Checkup");

        when(treatmentRepository.save(any(Treatment.class))).thenReturn(savedEntity);

        TreatmentDto result = treatmentService.createTreatment(inputDto);

        assertNotNull(result);
        assertEquals(treatmentId, result.getId());
        assertEquals("Checkup", result.getDescription());
        verify(treatmentRepository, times(1)).save(any(Treatment.class));
    }
}