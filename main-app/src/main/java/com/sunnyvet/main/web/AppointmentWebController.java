package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/appointments")
public class AppointmentWebController {

    private final AppointmentService appointmentService;

    public AppointmentWebController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "appointments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("appointment", new AppointmentDto());
        return "appointments/new";
    }

    @PostMapping
    public String createAppointment(@ModelAttribute AppointmentDto appointmentDto) {
        appointmentService.createAppointment(appointmentDto);
        return "redirect:/appointments";
    }
}