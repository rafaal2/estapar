// src/main/java/com/example/estapar/aplication/controllers/GarageImportController.java

package com.example.estapar.aplication.controllers;

import com.example.estapar.aplication.services.GarageService;
import com.example.estapar.domain.dtos.GarageImportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GarageImportController {

    @Autowired
    private GarageService garageService;

    @PostMapping("/import-garage")
    public ResponseEntity<?> importGarage(@RequestBody GarageImportDTO dto) {
        garageService.importGarageConfig(dto.getGarage(), dto.getSpots());
        return ResponseEntity.ok("Garagem importada com sucesso!");
    }
}
