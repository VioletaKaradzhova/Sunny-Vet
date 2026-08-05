package com.sunnyvet.main.web;

import com.sunnyvet.main.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}