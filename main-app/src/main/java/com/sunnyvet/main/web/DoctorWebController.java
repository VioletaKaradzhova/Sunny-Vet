package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.entity.Doctor;
import com.sunnyvet.main.repository.DoctorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.UUID;

@Controller
@RequestMapping("/doctors")
public class DoctorWebController {

    private final DoctorRepository doctorRepository;

    public DoctorWebController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorRepository.findAll());
        return "doctors/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctors/form";
    }

    @PostMapping("/new")
    public String saveDoctor(@Valid @ModelAttribute("doctor") Doctor doctor, BindingResult result) {
        if (result.hasErrors()) return "doctors/form";
        doctorRepository.save(doctor);
        return "redirect:/doctors";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        model.addAttribute("doctor", doctorRepository.findById(id).orElseThrow());
        return "doctors/form";
    }

    @PostMapping("/edit/{id}")
    public String updateDoctor(@PathVariable UUID id, @Valid @ModelAttribute("doctor") Doctor doctor, BindingResult result) {
        if (result.hasErrors()) return "doctors/form";
        doctor.setId(id);
        doctorRepository.save(doctor);
        return "redirect:/doctors";
    }

    @PostMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable UUID id) {
        doctorRepository.deleteById(id);
        return "redirect:/doctors";
    }
}