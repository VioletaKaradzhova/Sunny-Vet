package com.sunnyvet.main.event;

import org.springframework.context.ApplicationEvent;
import java.util.UUID;

public class AppointmentCreatedEvent extends ApplicationEvent {
    private final UUID appointmentId;

    public AppointmentCreatedEvent(Object source, UUID appointmentId) {
        super(source);
        this.appointmentId = appointmentId;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }
}