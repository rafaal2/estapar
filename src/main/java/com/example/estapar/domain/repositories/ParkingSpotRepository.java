package com.example.estapar.domain.repositories;

import com.example.estapar.domain.entities.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    Optional<ParkingSpot> findByLatAndLng(Double lat, Double lng);
}
