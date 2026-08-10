package com.sunnyvet.microservice.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "treatments")
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID petId;

    @Column(nullable = false)
    private String description;

    @Column
    private String medication;

    @Column(nullable = false)
    private LocalDateTime treatmentDate = LocalDateTime.now();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getPetId() { return petId; }
    public void setPetId(UUID petId) { this.petId = petId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }
    public LocalDateTime getTreatmentDate() { return treatmentDate; }
    public void setTreatmentDate(LocalDateTime treatmentDate) { this.treatmentDate = treatmentDate; }
}