package com.sunnyvet.main.service;

import com.sunnyvet.main.domain.dto.PetDto;
import com.sunnyvet.main.domain.entity.Pet;
import com.sunnyvet.main.repository.PetRepository;
import com.sunnyvet.main.service.impl.PetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    @Test
    void getPetById_WhenPetExists_ReturnsPetDto() {
        UUID id = UUID.randomUUID();
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName("Buddy");

        when(petRepository.findById(id)).thenReturn(Optional.of(pet));

        PetDto result = petService.getPetById(id);

        assertNotNull(result);
        assertEquals("Buddy", result.getName());
    }

    @Test
    void getPetById_WhenPetDoesNotExist_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(petRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> petService.getPetById(id));
    }

    @Test
    void createPet_ValidDto_SavesAndReturnsDto() {
        PetDto input = new PetDto();
        input.setName("Max");
        Pet savedEntity = new Pet();
        savedEntity.setId(UUID.randomUUID());

        when(petRepository.save(any(Pet.class))).thenReturn(savedEntity);

        petService.createPet(input);

        verify(petRepository, times(1)).save(any(Pet.class));
    }
}