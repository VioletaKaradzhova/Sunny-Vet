package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.dto.PetDto;
import com.sunnyvet.main.domain.entity.Owner;
import com.sunnyvet.main.domain.entity.UserEntity;
import com.sunnyvet.main.repository.OwnerRepository;
import com.sunnyvet.main.repository.UserRepository;
import com.sunnyvet.main.service.PetService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Comparator;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public String listPets(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/login";

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().contains("ADMIN"));

        List<PetDto> petsToDisplay;

        if (!isAdmin) {
            UserEntity user = userRepository.findAll().stream()
                    .filter(u -> u.getUsername().equals(authentication.getName()))
                    .findFirst().orElse(null);

            if (user != null) {
                Owner owner = ownerRepository.findAll().stream()
                        .filter(o -> o.getUser() != null && o.getUser().getId().equals(user.getId()))
                        .findFirst().orElse(null);

                if (owner != null) {
                    petsToDisplay = petService.getAllPets().stream()
                            .filter(p -> p.getOwnerId() != null && p.getOwnerId().equals(owner.getId()))
                            .collect(Collectors.toList());
                } else {
                    petsToDisplay = List.of();
                }
            } else {
                petsToDisplay = List.of();
            }
        } else {
            petsToDisplay = petService.getAllPets();
        }

        petsToDisplay.sort(Comparator.comparing(PetDto::getName, String.CASE_INSENSITIVE_ORDER));

        model.addAttribute("pets", petsToDisplay);
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
            if (bindingResult.getFieldErrors().stream().anyMatch(err -> !"ownerId".equals(err.getField()))) {
                return "pets/new";
            }
        }
        if (principal != null) {
            UserEntity user = userRepository.findAll().stream()
                    .filter(u -> u.getUsername().equals(principal.getName()))
                    .findFirst().orElse(null);

            if (user != null) {
                Owner owner = ownerRepository.findAll().stream()
                        .filter(o -> o.getUser() != null && o.getUser().getId().equals(user.getId()))
                        .findFirst().orElse(null);

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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model, Authentication auth) {
        if (!verifyOwnership(id, auth)) return "redirect:/pets?error=unauthorized";
        model.addAttribute("pet", petService.getPetById(id));
        return "pets/new";
    }

    @PostMapping("/edit/{id}")
    public String updatePet(@PathVariable UUID id, @Valid @ModelAttribute("pet") PetDto petDto, BindingResult bindingResult, Authentication auth) {
        if (!verifyOwnership(id, auth)) return "redirect:/pets?error=unauthorized";

        if (bindingResult.hasErrors()) {
            if (bindingResult.getFieldErrors().stream().anyMatch(err -> !"ownerId".equals(err.getField()))) {
                return "pets/new";
            }
        }

        petDto.setId(id);
        PetDto existing = petService.getPetById(id);
        petDto.setOwnerId(existing.getOwnerId());

        petService.updatePet(id, petDto);
        return "redirect:/pets";
    }

    @PostMapping("/delete/{id}")
    public String deletePet(@PathVariable UUID id, Authentication auth) {
        if (verifyOwnership(id, auth)) petService.deletePet(id);
        return "redirect:/pets";
    }

    private boolean verifyOwnership(UUID petId, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("ADMIN"));
        if (isAdmin) return true;

        PetDto pet = petService.getPetById(petId);

        Owner owner = ownerRepository.findAll().stream()
                .filter(o -> o.getUser() != null && o.getUser().getUsername().equals(auth.getName()))
                .findFirst().orElse(null);

        return owner != null && pet.getOwnerId() != null && pet.getOwnerId().equals(owner.getId());
    }
}