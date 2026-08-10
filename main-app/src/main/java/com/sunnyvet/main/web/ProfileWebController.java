package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.entity.UserEntity;
import com.sunnyvet.main.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileWebController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileWebController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showProfile(Model model, Principal principal) {
        UserEntity user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(principal.getName()))
                .findFirst().orElseThrow();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping
    public String updateProfile(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String fullName,
            @RequestParam String phoneNumber,
            @RequestParam(required = false) String password,
            Principal principal) {

        UserEntity user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(principal.getName()))
                .findFirst().orElseThrow();

        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);

        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);

        if (!username.equals(principal.getName())) {
            return "redirect:/logout";
        }

        return "redirect:/profile?success";
    }
}