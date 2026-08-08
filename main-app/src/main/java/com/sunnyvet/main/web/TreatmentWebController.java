package com.sunnyvet.main.web;

import com.sunnyvet.main.client.MicroserviceClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/treatments")
public class TreatmentWebController {

    private final MicroserviceClient microserviceClient;

    public TreatmentWebController(MicroserviceClient microserviceClient) {
        this.microserviceClient = microserviceClient;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", microserviceClient.getTreatmentStats());
        return "treatments/dashboard";
    }

    @PostMapping("/new")
    public String recordTreatment(@RequestParam String description, @RequestParam Double cost) {
        Map<String, Object> data = new HashMap<>();
        data.put("description", description);
        data.put("cost", cost);
        microserviceClient.recordTreatment(data);
        return "redirect:/treatments/dashboard";
    }

    @PostMapping("/update")
    public String updateTreatment(@RequestParam UUID id, @RequestParam String description, @RequestParam Double cost) {
        Map<String, Object> data = new HashMap<>();
        data.put("description", description);
        data.put("cost", cost);
        microserviceClient.updateTreatment(id, data);
        return "redirect:/treatments/dashboard";
    }
}