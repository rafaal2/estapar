package com.example.estapar.aplication.controllers;

import com.example.estapar.aplication.services.GarageService;
import com.example.estapar.domain.dtos.ParkingSectorDTO;
import com.example.estapar.domain.dtos.ParkingSpotDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/garage")
public class GarageController {

    @Autowired
    private GarageService garageService;

    @GetMapping("/sectors")
    public List<ParkingSectorDTO> getAllSectors() {
        return garageService.findAllSectors();
    }

    @GetMapping("/spots")
    public List<ParkingSpotDTO> getAllSpots() {
        return garageService.findAllSpots();
    }

    @PostMapping("/import")
    public void importGarage(@RequestBody GarageImportRequest request) {
        garageService.importGarageConfig(request.getSectors(), request.getSpots());
    }

    @Setter
    @Getter
    public static class GarageImportRequest {
        // Getters e setters
        private List<ParkingSectorDTO> sectors;
        private List<ParkingSpotDTO> spots;

    }
}
