package com.sunnyvet.microservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class TreatmentDto {
    private UUID id;

    @NotNull(message = "Pet ID is required")
    private UUID petId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Cost is required")
    @Positive(message = "Cost must be positive")
    private Double cost;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getPetId() { return petId; }
    public void setPetId(UUID petId) { this.petId = petId; }
    public UUID getDoctorId() { return doctorId; }
    public void setDoctorId(UUID doctorId) { this.doctorId = doctorId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }
}