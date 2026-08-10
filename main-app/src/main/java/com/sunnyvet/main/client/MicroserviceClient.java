package com.sunnyvet.main.client;

import com.sunnyvet.main.domain.dto.TreatmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "treatment-microservice", url = "http://localhost:8081/api/treatments")
public interface MicroserviceClient {

    @GetMapping("/pet/{petId}")
    List<TreatmentDto> getTreatmentsByPetId(@PathVariable("petId") UUID petId);

    @PostMapping
    TreatmentDto recordTreatment(@RequestBody TreatmentDto treatmentData);

    @PutMapping("/{id}")
    TreatmentDto updateTreatment(@PathVariable("id") UUID id, @RequestBody TreatmentDto treatmentData);

    @GetMapping("/stats")
    Map<String, Object> getTreatmentStats();
}