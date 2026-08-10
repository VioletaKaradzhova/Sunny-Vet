package com.sunnyvet.main.web;

import com.sunnyvet.main.repository.DoctorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DoctorRepository doctorRepository;

    public HomeController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("doctors", doctorRepository.findAll());
        return "index";
    }
}