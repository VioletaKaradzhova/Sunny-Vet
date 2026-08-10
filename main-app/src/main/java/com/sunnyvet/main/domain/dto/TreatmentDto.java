package com.sunnyvet.main.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TreatmentDto {
    private UUID id;
    private UUID petId;
    private String description;
    private String medication;
    private LocalDateTime treatmentDate;

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