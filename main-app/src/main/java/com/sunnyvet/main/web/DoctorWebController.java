package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.dto.DoctorFormDto;
import com.sunnyvet.main.domain.entity.Doctor;
import com.sunnyvet.main.domain.entity.UserEntity;
import com.sunnyvet.main.domain.enums.Role;
import com.sunnyvet.main.repository.DoctorRepository;
import com.sunnyvet.main.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/doctors")
public class DoctorWebController {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorWebController(DoctorRepository doctorRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("doctorForm", new DoctorFormDto());
        return "doctors/form";
    }

    @PostMapping("/new")
    public String saveDoctor(@ModelAttribute("doctorForm") DoctorFormDto dto) {
        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(Role.DOCTOR);
        user = userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setFullName(dto.getFullName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setUser(user);
        doctorRepository.save(doctor);

        return "redirect:/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow();
        UserEntity user = doctor.getUser();

        DoctorFormDto dto = new DoctorFormDto();
        dto.setId(doctor.getId());
        dto.setFullName(doctor.getFullName());
        dto.setSpecialization(doctor.getSpecialization());

        if (user != null) {
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setPhoneNumber(user.getPhoneNumber());
        }

        model.addAttribute("doctorForm", dto);
        return "doctors/form";
    }

    @PostMapping("/edit/{id}")
    public String updateDoctor(@PathVariable UUID id, @ModelAttribute("doctorForm") DoctorFormDto dto) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow();

        doctor.setFullName(dto.getFullName());
        doctor.setSpecialization(dto.getSpecialization());
        doctorRepository.save(doctor);

        UserEntity user = doctor.getUser();
        if (user != null) {
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            user.setFullName(dto.getFullName());
            user.setPhoneNumber(dto.getPhoneNumber());
            if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            userRepository.save(user);
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable UUID id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow();
        UserEntity user = doctor.getUser();

        doctorRepository.deleteById(id);
        if (user != null) {
            userRepository.delete(user);
        }
        return "redirect:/dashboard";
    }
}