package com.sunnyvet.main.domain.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class AppointmentDto {
    private UUID id;

    @NotNull(message = "Appointment time is required")
    @FutureOrPresent(message = "Appointment cannot be in the past")
    private LocalDateTime appointmentTime;

    @NotBlank(message = "Reason is required")
    private String reason;

    @NotNull(message = "Pet ID is required")
    private UUID petId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public UUID getPetId() { return petId; }
    public void setPetId(UUID petId) { this.petId = petId; }
    public UUID getDoctorId() { return doctorId; }
    public void setDoctorId(UUID doctorId) { this.doctorId = doctorId; }
}