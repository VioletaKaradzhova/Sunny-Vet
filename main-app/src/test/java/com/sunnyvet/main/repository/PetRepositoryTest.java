package com.sunnyvet.main.repository;

import com.sunnyvet.main.domain.entity.Owner;
import com.sunnyvet.main.domain.entity.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PetRepositoryTest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    void testSaveAndFindPet() {
        Owner owner = new Owner();
        owner.setFullName("John Doe");
        owner.setPhoneNumber("12345");
        Owner savedOwner = ownerRepository.save(owner);

        Pet pet = new Pet();
        pet.setName("Rex");
        pet.setSpecies("Dog");
        pet.setAge(3);
        pet.setOwner(savedOwner);

        Pet savedPet = petRepository.save(pet);
        Optional<Pet> foundPet = petRepository.findById(savedPet.getId());

        assertTrue(foundPet.isPresent());
        assertEquals("Rex", foundPet.get().getName());
        assertEquals(savedOwner.getId(), foundPet.get().getOwner().getId());
    }
}