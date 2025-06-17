package com.example.estapar.aplication.services;

import com.example.estapar.domain.dtos.ParkingSectorDTO;
import com.example.estapar.domain.dtos.ParkingSpotDTO;
import com.example.estapar.domain.entities.ParkingSector;
import com.example.estapar.domain.entities.ParkingSpot;
import com.example.estapar.domain.repositories.ParkingSectorRepository;
import com.example.estapar.domain.repositories.ParkingSpotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GarageService {

    @Autowired
    private ParkingSectorRepository sectorRepository;

    @Autowired
    private ParkingSpotRepository spotRepository;

    @Transactional(readOnly = true)
    public List<ParkingSectorDTO> findAllSectors() {
        List<ParkingSector> sectors = sectorRepository.findAll();
        return sectors.stream().map(ParkingSectorDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParkingSpotDTO> findAllSpots() {
        List<ParkingSpot> spots = spotRepository.findAll();
        return spots.stream().map(ParkingSpotDTO::new).collect(Collectors.toList());
    }

    private void copySectorDtoToEntity(ParkingSectorDTO dto, ParkingSector entity) {
        entity.setSector(dto.getSector());
        entity.setBasePrice(dto.getBasePrice());
        entity.setMaxCapacity(dto.getMaxCapacity());
        entity.setOpenHour(LocalTime.parse(dto.getOpenHour()));
        entity.setCloseHour(LocalTime.parse(dto.getCloseHour()));
        entity.setDurationLimitMinutes(dto.getDurationLimitMinutes());
    }

    private void copySpotDtoToEntity(ParkingSpotDTO dto, ParkingSpot entity) {
        entity.setLat(dto.getLat());
        entity.setLng(dto.getLng());
        entity.setOccupied(dto.getOccupied() != null ? dto.getOccupied() : false);
    }

    @Transactional
    public void importGarageConfig(List<ParkingSectorDTO> sectors, List<ParkingSpotDTO> spots) {
        for (ParkingSectorDTO sectorDTO : sectors) {
            Optional<ParkingSector> existing = sectorRepository.findBySector(sectorDTO.getSector());
            ParkingSector sector = existing.orElseGet(ParkingSector::new);
            copySectorDtoToEntity(sectorDTO, sector);
            sectorRepository.save(sector);
        }

        for (ParkingSpotDTO spotDTO : spots) {
            ParkingSector sector = sectorRepository.findBySector(spotDTO.getSectorName())
                    .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado: " + spotDTO.getSectorName()));
            ParkingSpot spot = new ParkingSpot();
            spot.setLat(spotDTO.getLat());
            spot.setLng(spotDTO.getLng());
            spot.setParkingSector(sector);
            spot.setOccupied(spotDTO.getOccupied() != null && spotDTO.getOccupied());
            spotRepository.save(spot);
        }
    }

}
