package com.sunnyvet.microservice.controller;

import com.sunnyvet.microservice.domain.dto.TreatmentDto;
import com.sunnyvet.microservice.service.TreatmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @PostMapping
    public ResponseEntity<TreatmentDto> recordTreatment(@RequestBody TreatmentDto treatmentDto) {
        TreatmentDto createdTreatment = treatmentService.createTreatment(treatmentDto);
        return new ResponseEntity<>(createdTreatment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentDto> updateTreatment(@PathVariable UUID id, @RequestBody TreatmentDto treatmentDto) {
        TreatmentDto updatedTreatment = treatmentService.updateTreatment(id, treatmentDto);
        return ResponseEntity.ok(updatedTreatment);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getTreatmentStats() {
        return ResponseEntity.ok(treatmentService.getTreatmentStats());
    }
}