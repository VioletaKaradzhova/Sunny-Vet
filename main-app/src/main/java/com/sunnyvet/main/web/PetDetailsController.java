package com.sunnyvet.main.web;

import com.sunnyvet.main.repository.PetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class PetDetailsController {

    private final PetRepository petRepository;

    public PetDetailsController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @GetMapping("/pets/{id}")
    public String viewPet(@PathVariable UUID id, Model model) {
        model.addAttribute("pet", petRepository.findById(id).orElse(null));
        return "pets/details";
    }
}