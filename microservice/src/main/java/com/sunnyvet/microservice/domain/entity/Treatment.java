package com.sunnyvet.microservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "treatments")
public class Treatment {
    @Id
    private UUID id;
    private UUID petId;
    private UUID doctorId;
    private String description;
    private Double cost;

    public Treatment() {
        this.id = UUID.randomUUID();
    }

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