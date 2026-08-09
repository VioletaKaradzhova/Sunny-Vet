package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.entity.UserEntity;
import com.sunnyvet.main.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationWebController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationWebController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password) {

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        user.setRole(com.sunnyvet.main.domain.enums.Role.USER);

        userRepository.save(user);

        return "redirect:/login";
    }
}