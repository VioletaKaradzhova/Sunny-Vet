package com.sunnyvet.main.web;

import com.sunnyvet.main.repository.DoctorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}