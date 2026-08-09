package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.dto.PetDto;
import com.sunnyvet.main.domain.entity.Owner;
import com.sunnyvet.main.domain.entity.UserEntity;
import com.sunnyvet.main.repository.OwnerRepository;
import com.sunnyvet.main.repository.UserRepository;
import com.sunnyvet.main.service.PetService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/pets")
public class PetWebController {

    private final PetService petService;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;

    public PetWebController(PetService petService, OwnerRepository ownerRepository, UserRepository userRepository) {
        this.petService = petService;
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
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
    public String createPet(@Valid @ModelAttribute("pet") PetDto petDto, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            boolean hasRealErrors = bindingResult.getFieldErrors().stream()
                    .anyMatch(err -> !"ownerId".equals(err.getField()));

            if (hasRealErrors) {
                return "pets/new";
            }
        }

        if (principal != null) {
            UserEntity user = userRepository.findAll().stream()
                    .filter(u -> u.getUsername().equals(principal.getName()))
                    .findFirst()
                    .orElse(null);

            if (user != null) {
                Owner owner = ownerRepository.findAll().stream()
                        .filter(o -> o.getUser() != null && o.getUser().getId().equals(user.getId()))
                        .findFirst()
                        .orElse(null);

                if (owner == null) {
                    owner = new Owner();
                    owner.setFullName(user.getUsername());
                    owner.setPhoneNumber("N/A");
                    owner.setUser(user);
                    owner = ownerRepository.save(owner);
                }

                petDto.setOwnerId(owner.getId());
            }
        }

        petService.createPet(petDto);
        return "redirect:/pets";
    }
}