package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.entity.Doctor;
import com.sunnyvet.main.domain.entity.Pet;
import com.sunnyvet.main.repository.DoctorRepository;
import com.sunnyvet.main.repository.PetRepository;
import com.sunnyvet.main.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sunnyvet.main.domain.entity.UserEntity;
import com.sunnyvet.main.repository.UserRepository;
import java.util.Comparator;
import java.util.List;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class AdminWebController {

    private final DoctorRepository doctorRepository;
    private final PetRepository petRepository;
    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    public AdminWebController(DoctorRepository doctorRepository, PetRepository petRepository, AppointmentService appointmentService, UserRepository userRepository) {
        this.doctorRepository = doctorRepository;
        this.petRepository = petRepository;
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("pets", petRepository.findAll());
        model.addAttribute("appointments", appointmentService.getAllAppointments());

        Map<UUID, String> docNames = doctorRepository.findAll().stream().collect(Collectors.toMap(Doctor::getId, Doctor::getFullName));
        Map<UUID, String> petNames = petRepository.findAll().stream().collect(Collectors.toMap(Pet::getId, Pet::getName));

        model.addAttribute("doctorNames", docNames);
        model.addAttribute("petNames", petNames);

        List<UserEntity> users = userRepository.findAll();
        users.sort(Comparator.comparing(UserEntity::getFullName, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("users", users);

        return "admin/dashboard";
    }
}