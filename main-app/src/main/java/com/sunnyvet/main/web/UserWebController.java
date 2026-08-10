package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.entity.UserEntity;
import com.sunnyvet.main.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserWebController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserWebController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        return "users/form";
    }

    @PostMapping("/new")
    public String saveUser(@ModelAttribute("userEntity") UserEntity user, @RequestParam String rawPassword) {
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
        return "redirect:/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        model.addAttribute("userEntity", userRepository.findById(id).orElseThrow());
        return "users/form";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable UUID id, @ModelAttribute("userEntity") UserEntity user, @RequestParam(required = false) String rawPassword) {
        UserEntity existing = userRepository.findById(id).orElseThrow();

        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        existing.setFullName(user.getFullName());
        existing.setPhoneNumber(user.getPhoneNumber());
        existing.setRole(user.getRole());

        if (rawPassword != null && !rawPassword.trim().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(rawPassword));
        }

        userRepository.save(existing);
        return "redirect:/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable UUID id) {
        userRepository.deleteById(id);
        return "redirect:/dashboard";
    }
}