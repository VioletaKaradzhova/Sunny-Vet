package com.sunnyvet.main.domain.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class AppointmentDto {

    private UUID id;

    @NotNull(message = "Pet ID cannot be null")
    private UUID petId;

    @NotNull(message = "Doctor ID cannot be null")
    private UUID doctorId;

    @NotNull(message = "Appointment time cannot be null")
    @Future(message = "Appointment must be in the future")
    private LocalDateTime appointmentTime;

    @NotBlank(message = "Reason cannot be blank")
    private String reason;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPetId() {
        return petId;
    }

    public void setPetId(UUID petId) {
        this.petId = petId;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}