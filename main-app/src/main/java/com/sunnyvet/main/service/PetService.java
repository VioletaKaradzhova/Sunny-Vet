package com.sunnyvet.main.service;

import com.sunnyvet.main.domain.dto.PetDto;
import java.util.List;
import java.util.UUID;

public interface PetService {
    PetDto createPet(PetDto petDto);
    PetDto getPetById(UUID id);
    List<PetDto> getPetsByOwnerId(UUID ownerId);
}