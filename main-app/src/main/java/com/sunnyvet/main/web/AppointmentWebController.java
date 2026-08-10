package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.domain.entity.Pet;
import com.sunnyvet.main.repository.DoctorRepository;
import com.sunnyvet.main.repository.PetRepository;
import com.sunnyvet.main.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/appointments")
public class AppointmentWebController {

    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;
    private final PetRepository petRepository;

    public AppointmentWebController(AppointmentService appointmentService, DoctorRepository doctorRepository, PetRepository petRepository) {
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
        this.petRepository = petRepository;
    }

    @GetMapping
    public String listAppointments(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER") || a.getAuthority().equals("USER"));

        if (isUser) {
            List<Pet> userPets = petRepository.findAll().stream()
                    .filter(pet -> pet.getOwner() != null
                            && pet.getOwner().getUser() != null
                            && pet.getOwner().getUser().getUsername().equals(authentication.getName()))
                    .collect(Collectors.toList());

            List<UUID> userPetIds = userPets.stream().map(Pet::getId).collect(Collectors.toList());

            List<AppointmentDto> filtered = appointmentService.getAllAppointments().stream()
                    .filter(appt -> userPetIds.contains(appt.getPetId()))
                    .collect(Collectors.toList());
            model.addAttribute("appointments", filtered);
        } else {
            model.addAttribute("appointments", appointmentService.getAllAppointments());
        }

        return "appointments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model, Principal principal) {
        List<Pet> userPets = petRepository.findAll().stream()
                .filter(pet -> pet.getOwner() != null
                        && pet.getOwner().getUser() != null
                        && pet.getOwner().getUser().getUsername().equals(principal.getName()))
                .collect(Collectors.toList());

        model.addAttribute("appointment", new AppointmentDto());
        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("pets", userPets);
        return "appointments/new";
    }

    @PostMapping
    public String createAppointment(@Valid @ModelAttribute("appointment") AppointmentDto appointmentDto, BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            List<Pet> userPets = petRepository.findAll().stream()
                    .filter(pet -> pet.getOwner() != null
                            && pet.getOwner().getUser() != null
                            && pet.getOwner().getUser().getUsername().equals(principal.getName()))
                    .collect(Collectors.toList());

            model.addAttribute("doctors", doctorRepository.findAll());
            model.addAttribute("pets", userPets);
            return "appointments/new";
        }
        appointmentService.createAppointment(appointmentDto);
        return "redirect:/appointments";
    }
}