package com.sunnyvet.main.repository;

import com.sunnyvet.main.domain.entity.Owner;
import com.sunnyvet.main.domain.entity.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class PetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PetRepository petRepository;

    @Test
    void findByOwnerId_ReturnsPets() {
        Owner owner = new Owner();
        owner.setFullName("John Doe");
        owner.setPhoneNumber("555-0199");
        entityManager.persistAndFlush(owner);

        Pet pet = new Pet();
        pet.setName("Rex");
        pet.setSpecies("Dog");
        pet.setOwner(owner);
        entityManager.persistAndFlush(pet);

        List<Pet> results = petRepository.findByOwnerId(owner.getId());

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(owner.getId(), results.get(0).getOwner().getId());
    }
}