package com.example.estapar.aplication.controllers;

import com.example.estapar.aplication.services.ParkingSessionService;
import com.example.estapar.aplication.services.WebhookService;
import com.example.estapar.domain.dtos.WebhookEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private WebhookService webhookService;

    @PostMapping
    public ResponseEntity<?> handleWebhook(@RequestBody WebhookEventDto event) {
        webhookService.processWebhook(event);
        return ResponseEntity.ok().build();
    }
}
