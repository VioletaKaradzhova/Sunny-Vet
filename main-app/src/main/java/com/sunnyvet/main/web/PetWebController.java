package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.dto.PetDto;
import com.sunnyvet.main.service.PetService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pets")
public class PetWebController {

    private final PetService petService;

    public PetWebController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public String listPets(Model model) {
        model.addAttribute("pets", petService.getAllPets());
        return "pets/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("pet", new PetDto());
        return "pets/new";
    }

    @PostMapping
    public String createPet(@Valid @ModelAttribute("pet") PetDto petDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pets/new";
        }
        petService.createPet(petDto);
        return "redirect:/pets";
    }
}