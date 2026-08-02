package com.sunnyvet.main.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "treatment-microservice", url = "http://localhost:8081/api/treatments")
public interface MicroserviceClient {

    @PostMapping
    Map<String, Object> recordTreatment(@RequestBody Map<String, Object> treatmentData);

    @GetMapping("/stats")
    Map<String, Object> getTreatmentStats();
}