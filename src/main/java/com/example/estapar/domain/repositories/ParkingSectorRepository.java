package com.example.estapar.domain.repositories;

import com.example.estapar.domain.entities.ParkingSector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSectorRepository extends JpaRepository<ParkingSector, Long> {
    Optional<ParkingSector> findBySector(String sectorName);
}
