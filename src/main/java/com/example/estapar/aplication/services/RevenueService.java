package com.example.estapar.aplication.services;

import com.example.estapar.domain.dtos.RevenueDTO;
import com.example.estapar.domain.entities.ParkingSector;
import com.example.estapar.domain.entities.ParkingSession;
import com.example.estapar.domain.repositories.ParkingSectorRepository;
import com.example.estapar.domain.repositories.ParkingSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RevenueService {

    @Autowired
    private ParkingSessionRepository sessionRepository;

    @Autowired
    private ParkingSectorRepository sectorRepository;

    @Transactional(readOnly = true)
    public RevenueDTO getRevenue(String sectorName, LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay();

        List<ParkingSession> sessions;

        if (sectorName == null || sectorName.isBlank()) {
            sessions = sessionRepository.findAllByExitTimeBetween(start, end);
        } else {
            ParkingSector sector = sectorRepository.findBySector(sectorName.trim().toUpperCase())
                    .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado: " + sectorName));
            sessions = sessionRepository.findAllByParkingSectorAndExitTimeBetween(sector, start, end);
        }
        double amount = sessions.stream()
                .mapToDouble(this::valorLiquidado)
                .sum();

        RevenueDTO dto = new RevenueDTO();
        dto.setAmount(amount);
        dto.setCurrency("BRL");
        dto.setTimestamp(LocalDateTime.now().toString());
        return dto;
    }
    private double valorLiquidado(ParkingSession s) {

        LocalDateTime inicio = s.getParkedTime() != null ? s.getParkedTime()
                : s.getEntryTime();

        LocalDateTime fim = s.getExitTime() != null ? s.getExitTime()
                : LocalDateTime.now();

        Duration dur = Duration.between(inicio, fim);

        return (dur.toMinutes() / 60.0) * s.getPriceCharged();
    }
}
