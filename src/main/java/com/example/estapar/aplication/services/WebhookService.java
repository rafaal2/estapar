package com.example.estapar.aplication.services;

import com.example.estapar.domain.dtos.WebhookEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    @Autowired
    private ParkingSessionService sessionService;

    public void processWebhook(WebhookEventDto event) {
        switch (event.getEventType()) {
            case "ENTRY":
                sessionService.handleEntryEvent(event);
                break;
            case "PARKED":
                sessionService.handleParkedEvent(event);
                break;
            case "EXIT":
                sessionService.handleExitEvent(event);
                break;
            default:
                throw new IllegalArgumentException("Evento desconhecido: " + event.getEventType());
        }
    }
}
