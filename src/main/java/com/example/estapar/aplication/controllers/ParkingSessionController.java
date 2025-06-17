package com.example.estapar.aplication.controllers;

import com.example.estapar.aplication.services.ParkingSessionService;
import com.example.estapar.domain.dtos.PlateStatusRequest;
import com.example.estapar.domain.dtos.SpotStatusRequest;
import com.example.estapar.domain.dtos.PlateStatusResponse;
import com.example.estapar.domain.dtos.SpotStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParkingSessionController {

    @Autowired
    private ParkingSessionService sessionService;

    @PostMapping("/plate-status")
    public PlateStatusResponse getPlateStatus(@RequestBody PlateStatusRequest request) {
        return sessionService.getPlateStatus(request.getLicense_plate());
    }

    @PostMapping("/spot-status")
    public SpotStatusResponse getSpotStatus(@RequestBody SpotStatusRequest request) {
        return sessionService.getSpotStatus(request.getLat(), request.getLng());
    }
}
