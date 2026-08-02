package com.sunnyvet.main.service.impl;

import com.sunnyvet.main.domain.dto.PetDto;
import com.sunnyvet.main.domain.entity.Owner;
import com.sunnyvet.main.domain.entity.Pet;
import com.sunnyvet.main.exception.ResourceNotFoundException;
import com.sunnyvet.main.repository.OwnerRepository;
import com.sunnyvet.main.repository.PetRepository;
import com.sunnyvet.main.service.PetService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    public PetServiceImpl(PetRepository petRepository, OwnerRepository ownerRepository) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    @CacheEvict(value = "pets", allEntries = true)
    public PetDto createPet(PetDto petDto) {
        Owner owner = ownerRepository.findById(petDto.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        Pet pet = new Pet();
        pet.setName(petDto.getName());
        pet.setSpecies(petDto.getSpecies());
        pet.setAge(petDto.getAge());
        pet.setOwner(owner);

        Pet savedPet = petRepository.save(pet);
        return mapToDto(savedPet);
    }

    @Override
    @Cacheable(value = "pets", key = "#id")
    public PetDto getPetById(UUID id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        return mapToDto(pet);
    }

    @Override
    public List<PetDto> getPetsByOwnerId(UUID ownerId) {
        return petRepository.findByOwnerId(ownerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PetDto mapToDto(Pet pet) {
        PetDto dto = new PetDto();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setSpecies(pet.getSpecies());
        dto.setAge(pet.getAge());
        dto.setOwnerId(pet.getOwner().getId());
        return dto;
    }
}