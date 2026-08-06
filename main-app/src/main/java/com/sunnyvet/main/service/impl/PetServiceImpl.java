package com.sunnyvet.main.service.impl;

import com.sunnyvet.main.domain.dto.PetDto;
import com.sunnyvet.main.domain.entity.Owner;
import com.sunnyvet.main.domain.entity.Pet;
import com.sunnyvet.main.exception.ResourceNotFoundException;
import com.sunnyvet.main.repository.PetRepository;
import com.sunnyvet.main.service.PetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public PetDto createPet(PetDto petDto) {
        Pet pet = new Pet();
        pet.setName(petDto.getName());
        pet.setSpecies(petDto.getSpecies());
        pet.setAge(petDto.getAge());

        if (petDto.getOwnerId() != null) {
            Owner owner = new Owner();
            owner.setId(petDto.getOwnerId());
            pet.setOwner(owner);
        }

        Pet savedPet = petRepository.save(pet);
        return mapToDto(savedPet);
    }

    @Override
    public PetDto getPetById(UUID id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with ID: " + id));
        return mapToDto(pet);
    }

    @Override
    public List<PetDto> getAllPets() {
        return petRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PetDto updatePet(UUID id, PetDto petDto) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with ID: " + id));

        pet.setName(petDto.getName());
        pet.setSpecies(petDto.getSpecies());
        pet.setAge(petDto.getAge());

        if (petDto.getOwnerId() != null) {
            Owner owner = new Owner();
            owner.setId(petDto.getOwnerId());
            pet.setOwner(owner);
        }

        Pet updatedPet = petRepository.save(pet);
        return mapToDto(updatedPet);
    }

    @Override
    public void deletePet(UUID id) {
        petRepository.deleteById(id);
    }

    private PetDto mapToDto(Pet pet) {
        PetDto dto = new PetDto();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setSpecies(pet.getSpecies());
        dto.setAge(pet.getAge());
        if (pet.getOwner() != null) {
            dto.setOwnerId(pet.getOwner().getId());
        }
        return dto;
    }
}