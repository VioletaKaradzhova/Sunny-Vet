package com.sunnyvet.main.controller;

import com.sunnyvet.main.domain.dto.AppointmentDto;
import com.sunnyvet.main.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<AppointmentDto>> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {
        AppointmentDto createdAppointment = appointmentService.createAppointment(appointmentDto);
        return new ResponseEntity<>(toModel(createdAppointment), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AppointmentDto>> getAppointment(@PathVariable UUID id) {
        AppointmentDto appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(toModel(appointment));
    }

    private EntityModel<AppointmentDto> toModel(AppointmentDto appointmentDto) {
        return EntityModel.of(appointmentDto,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AppointmentController.class).getAppointment(appointmentDto.getId())).withSelfRel());
    }
}