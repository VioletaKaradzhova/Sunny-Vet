package com.sunnyvet.main.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "treatment-microservice", url = "http://localhost:8081/api/treatments")
public interface MicroserviceClient {

    @PostMapping
    Map<String, Object> recordTreatment(@RequestBody Map<String, Object> treatmentData);

    @PutMapping("/{id}")
    Map<String, Object> updateTreatment(@PathVariable("id") UUID id, @RequestBody Map<String, Object> treatmentData);

    @GetMapping("/stats")
    Map<String, Object> getTreatmentStats();
}