package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.repository.DoctorRepository;
import com.sunnyvet.main.repository.PetRepository;
import com.sunnyvet.main.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/appointments")
public class AppointmentWebController {

    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;
    private final PetRepository petRepository;

    public AppointmentWebController(AppointmentService appointmentService, DoctorRepository doctorRepository, PetRepository petRepository) {
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
        this.petRepository = petRepository;
    }

    @GetMapping
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "appointments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("appointment", new AppointmentDto());
        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("pets", petRepository.findAll());
        return "appointments/new";
    }

    @PostMapping
    public String createAppointment(@Valid @ModelAttribute("appointment") AppointmentDto appointmentDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctors", doctorRepository.findAll());
            model.addAttribute("pets", petRepository.findAll());
            return "appointments/new";
        }
        appointmentService.createAppointment(appointmentDto);
        return "redirect:/appointments";
    }
}