package com.sunnyvet.main.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class PetDto {

    private UUID id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Species cannot be blank")
    private String species;

    @NotNull(message = "Age cannot be null")
    @Positive(message = "Age must be positive")
    private Integer age;

    @NotNull(message = "Owner ID cannot be null")
    private UUID ownerId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }
}