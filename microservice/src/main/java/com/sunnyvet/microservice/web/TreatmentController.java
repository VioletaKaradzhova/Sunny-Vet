package com.sunnyvet.microservice.web;

import com.sunnyvet.microservice.domain.entity.Treatment;
import com.sunnyvet.microservice.domain.dto.TreatmentDto;
import com.sunnyvet.microservice.repository.TreatmentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentRepository treatmentRepository;

    public TreatmentController(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    @GetMapping("/pet/{petId}")
    public List<TreatmentDto> getTreatmentsByPetId(@PathVariable UUID petId) {
        return treatmentRepository.findByPetIdOrderByTreatmentDateDesc(petId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @PostMapping
    public TreatmentDto recordTreatment(@RequestBody TreatmentDto dto) {
        Treatment treatment = new Treatment();
        treatment.setPetId(dto.getPetId());
        treatment.setDescription(dto.getDescription());
        treatment.setMedication(dto.getMedication());
        treatment.setTreatmentDate(LocalDateTime.now());
        return mapToDto(treatmentRepository.save(treatment));
    }

    @PutMapping("/{id}")
    public TreatmentDto updateTreatment(@PathVariable UUID id, @RequestBody TreatmentDto dto) {
        Treatment treatment = treatmentRepository.findById(id).orElseThrow();
        treatment.setDescription(dto.getDescription());
        treatment.setMedication(dto.getMedication());
        return mapToDto(treatmentRepository.save(treatment));
    }

    @GetMapping("/stats")
    public Map<String, Object> getTreatmentStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTreatments", treatmentRepository.count());
        return stats;
    }

    private TreatmentDto mapToDto(Treatment treatment) {
        TreatmentDto dto = new TreatmentDto();
        dto.setId(treatment.getId());
        dto.setPetId(treatment.getPetId());
        dto.setDescription(treatment.getDescription());
        dto.setMedication(treatment.getMedication());
        dto.setTreatmentDate(treatment.getTreatmentDate());
        return dto;
    }
}