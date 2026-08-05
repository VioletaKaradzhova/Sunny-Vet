package com.sunnyvet.main.service;

import com.sunnyvet.main.domain.dto.PetDto;
import com.sunnyvet.main.domain.entity.Owner;
import com.sunnyvet.main.domain.entity.Pet;
import com.sunnyvet.main.repository.OwnerRepository;
import com.sunnyvet.main.repository.PetRepository;
import com.sunnyvet.main.service.impl.PetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private PetServiceImpl petService;

    @Test
    void getPetById_WhenPetExists_ReturnsPetDto() {
        UUID petId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Owner mockOwner = new Owner();
        mockOwner.setId(ownerId);

        Pet mockPetEntity = new Pet();
        mockPetEntity.setId(petId);
        mockPetEntity.setName("Buddy");
        mockPetEntity.setSpecies("Dog");
        mockPetEntity.setAge(5);
        mockPetEntity.setOwner(mockOwner);

        when(petRepository.findById(petId)).thenReturn(Optional.of(mockPetEntity));

        PetDto result = petService.getPetById(petId);

        assertNotNull(result);
        assertEquals(petId, result.getId());
        assertEquals("Buddy", result.getName());
        assertEquals("Dog", result.getSpecies());
        assertEquals(5, result.getAge());

        verify(petRepository, times(1)).findById(petId);
    }

    @Test
    void getPetById_WhenPetDoesNotExist_ThrowsException() {
        UUID petId = UUID.randomUUID();
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> petService.getPetById(petId));

        verify(petRepository, times(1)).findById(petId);
    }

    @Test
    void createPet_ValidDto_SavesAndReturnsDto() {
        UUID newId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        PetDto incomingDto = new PetDto();
        incomingDto.setName("Bella");
        incomingDto.setSpecies("Cat");
        incomingDto.setAge(2);
        incomingDto.setOwnerId(ownerId);

        Owner mockOwner = new Owner();
        mockOwner.setId(ownerId);

        Pet savedEntity = new Pet();
        savedEntity.setId(newId);
        savedEntity.setName("Bella");
        savedEntity.setSpecies("Cat");
        savedEntity.setAge(2);
        savedEntity.setOwner(mockOwner);

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(mockOwner));
        when(petRepository.save(any(Pet.class))).thenReturn(savedEntity);

        PetDto result = petService.createPet(incomingDto);

        assertNotNull(result);
        assertEquals(newId, result.getId());
        assertEquals("Bella", result.getName());

        verify(ownerRepository, times(1)).findById(ownerId);
        verify(petRepository, times(1)).save(any(Pet.class));
    }
}