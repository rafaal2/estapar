package com.example.estapar.domain.repositories;

import com.example.estapar.domain.entities.ParkingSector;
import com.example.estapar.domain.entities.ParkingSession;
import com.example.estapar.domain.entities.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {
    int countByParkingSectorIdAndExitTimeIsNull(Long sectorId);
    List<ParkingSession> findAllByParkingSectorAndExitTimeBetween(
            ParkingSector sector,
            LocalDateTime start,
            LocalDateTime end
    );

    List<ParkingSession> findAllByExitTimeBetween(LocalDateTime start, LocalDateTime end);
    Optional<ParkingSession> findTopByVehicle_LicensePlateOrderByEntryTimeDesc(String licensePlate);
    Optional<ParkingSession> findTopBySpotAndExitTimeIsNull(ParkingSpot spot);
}
