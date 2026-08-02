package com.sunnyvet.main.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentEventListener.class);

    @EventListener
    public void handleAppointmentCreatedEvent(AppointmentCreatedEvent event) {
        logger.info("Received event for new appointment ID: {}", event.getAppointmentId());
    }
}