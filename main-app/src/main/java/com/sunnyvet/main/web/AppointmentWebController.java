package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.domain.entity.Doctor;
import com.sunnyvet.main.domain.entity.Pet;
import com.sunnyvet.main.repository.DoctorRepository;
import com.sunnyvet.main.repository.PetRepository;
import com.sunnyvet.main.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
        if (authentication == null) return "redirect:/login";
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("ADMIN"));

        List<AppointmentDto> appointments = appointmentService.getAllAppointments();

        if (!isAdmin) {
            List<Pet> userPets = getPrincipalPets(authentication.getName());
            List<UUID> userPetIds = userPets.stream().map(Pet::getId).collect(Collectors.toList());
            appointments = appointments.stream().filter(a -> userPetIds.contains(a.getPetId())).collect(Collectors.toList());
        }

        Map<UUID, String> docNames = doctorRepository.findAll().stream().collect(Collectors.toMap(Doctor::getId, Doctor::getFullName));
        Map<UUID, String> petNames = petRepository.findAll().stream().collect(Collectors.toMap(Pet::getId, Pet::getName));

        appointments.sort(Comparator.comparing(AppointmentDto::getAppointmentTime));

        model.addAttribute("appointments", appointments);
        model.addAttribute("doctorNames", docNames);
        model.addAttribute("petNames", petNames);
        return "appointments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) UUID doctorId, Model model, Principal principal) {

        AppointmentDto dto = new AppointmentDto();
        if (doctorId != null) {
            dto.setDoctorId(doctorId);
        }
        model.addAttribute("appointment", dto);
        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("pets", getPrincipalPets(principal.getName()));
        return "appointments/new";
    }

    @PostMapping
    public String createAppointment(@Valid @ModelAttribute("appointment") AppointmentDto dto, BindingResult result, Model model, Principal principal) {
        validateFutureDate(dto, result);
        if (result.hasErrors()) {
            model.addAttribute("doctors", doctorRepository.findAll());
            model.addAttribute("pets", getPrincipalPets(principal.getName()));
            return "appointments/new";
        }
        appointmentService.createAppointment(dto);
        return "redirect:/appointments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model, Authentication auth) {
        if (!verifyOwnership(id, auth)) return "redirect:/appointments?error=unauthorized";

        AppointmentDto appointment = appointmentService.getAllAppointments().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElseThrow();

        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("ADMIN"));

        model.addAttribute("appointment", appointment);
        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("pets", isAdmin ? petRepository.findAll() : getPrincipalPets(auth.getName()));
        return "appointments/new";
    }

    @PostMapping("/edit/{id}")
    public String updateAppointment(@PathVariable UUID id, @Valid @ModelAttribute("appointment") AppointmentDto dto, BindingResult result, Model model, Authentication auth) {
        if (!verifyOwnership(id, auth)) return "redirect:/appointments?error=unauthorized";
        validateFutureDate(dto, result);

        if (result.hasErrors()) {
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("ADMIN"));
            model.addAttribute("doctors", doctorRepository.findAll());
            model.addAttribute("pets", isAdmin ? petRepository.findAll() : getPrincipalPets(auth.getName()));
            return "appointments/new";
        }
        dto.setId(id);
        appointmentService.updateAppointment(id, dto);
        return "redirect:/appointments";
    }

    @PostMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable UUID id, Authentication auth) {
        if (verifyOwnership(id, auth)) appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }

    private List<Pet> getPrincipalPets(String username) {
        return petRepository.findAll().stream()
                .filter(pet -> pet.getOwner() != null && pet.getOwner().getUser() != null && pet.getOwner().getUser().getUsername().equals(username))
                .collect(Collectors.toList());
    }

    private void validateFutureDate(AppointmentDto dto, BindingResult result) {
        if (dto.getAppointmentTime() != null && dto.getAppointmentTime().isBefore(LocalDateTime.now())) {
            result.rejectValue("appointmentTime", "error.appointment", "The appointment date and time must be in the future.");
        }
    }

    private boolean verifyOwnership(UUID appointmentId, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("ADMIN"));
        if (isAdmin) return true;

        AppointmentDto appt = appointmentService.getAllAppointments().stream().filter(a -> a.getId().equals(appointmentId)).findFirst().orElseThrow();
        List<UUID> userPetIds = getPrincipalPets(auth.getName()).stream().map(Pet::getId).collect(Collectors.toList());
        return userPetIds.contains(appt.getPetId());
    }
}