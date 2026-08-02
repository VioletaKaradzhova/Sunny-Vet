package com.sunnyvet.main.controller;

import com.sunnyvet.main.domain.dto.PetDto;
import com.sunnyvet.main.service.PetService;
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
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<PetDto>> createPet(@Valid @RequestBody PetDto petDto) {
        PetDto createdPet = petService.createPet(petDto);
        EntityModel<PetDto> entityModel = EntityModel.of(createdPet);
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PetController.class).getPet(createdPet.getId())).withSelfRel());
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PetDto>> getPet(@PathVariable UUID id) {
        PetDto pet = petService.getPetById(id);
        EntityModel<PetDto> entityModel = EntityModel.of(pet);
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PetController.class).getPet(id)).withSelfRel());
        return ResponseEntity.ok(entityModel);
    }
}