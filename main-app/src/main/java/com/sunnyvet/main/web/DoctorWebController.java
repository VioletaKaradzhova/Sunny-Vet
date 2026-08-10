package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.entity.Doctor;
import com.sunnyvet.main.repository.DoctorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/doctors")
public class DoctorWebController {

    private final DoctorRepository doctorRepository;

    public DoctorWebController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctors/form";
    }

    @PostMapping
    public String saveDoctor(@ModelAttribute("doctor") Doctor doctor) {
        doctorRepository.save(doctor);
        return "redirect:/#doctors";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow();
        model.addAttribute("doctor", doctor);
        return "doctors/form";
    }

    @PostMapping("/edit/{id}")
    public String updateDoctor(@PathVariable UUID id, @ModelAttribute("doctor") Doctor doctor) {
        doctor.setId(id);
        doctorRepository.save(doctor);
        return "redirect:/#doctors";
    }

    @PostMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable UUID id) {
        doctorRepository.deleteById(id);
        return "redirect:/#doctors";
    }
}