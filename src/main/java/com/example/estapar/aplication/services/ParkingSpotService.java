package com.example.estapar.aplication.services;

import com.example.estapar.domain.dtos.ParkingSpotDTO;
import com.example.estapar.domain.entities.ParkingSector;
import com.example.estapar.domain.entities.ParkingSpot;
import com.example.estapar.domain.repositories.ParkingSectorRepository;
import com.example.estapar.domain.repositories.ParkingSpotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParkingSpotService {

    @Autowired
    private ParkingSpotRepository spotRepository;

    @Autowired
    private ParkingSectorRepository sectorRepository;

    @Transactional(readOnly = true)
    public ParkingSpotDTO findById(Long id) {
        ParkingSpot spot = spotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada: " + id));
        return new ParkingSpotDTO(spot);
    }

    @Transactional
    public ParkingSpotDTO save(ParkingSpotDTO dto) {
        ParkingSpot entity = new ParkingSpot();
        copyDtoToEntity(dto, entity);
        entity = spotRepository.save(entity);
        return new ParkingSpotDTO(entity);
    }

    @Transactional
    public ParkingSpotDTO update(Long id, ParkingSpotDTO dto) {
        ParkingSpot entity = spotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada: " + id));
        copyDtoToEntity(dto, entity);
        entity = spotRepository.save(entity);
        return new ParkingSpotDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!spotRepository.existsById(id)) {
            throw new EntityNotFoundException("Vaga não encontrada: " + id);
        }
        spotRepository.deleteById(id);
    }

    private void copyDtoToEntity(ParkingSpotDTO dto, ParkingSpot entity) {
        entity.setLat(dto.getLat());
        entity.setLng(dto.getLng());
        entity.setOccupied(dto.getOccupied() != null ? dto.getOccupied() : false);
    }
}
