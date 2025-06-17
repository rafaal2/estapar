package com.example.estapar.aplication.services;

import com.example.estapar.domain.dtos.PlateStatusResponse;
import com.example.estapar.domain.dtos.SpotStatusResponse;
import com.example.estapar.domain.dtos.WebhookEventDto;
import com.example.estapar.domain.entities.ParkingSector;
import com.example.estapar.domain.entities.ParkingSession;
import com.example.estapar.domain.entities.ParkingSpot;
import com.example.estapar.domain.entities.Vehicle;
import com.example.estapar.domain.repositories.ParkingSectorRepository;
import com.example.estapar.domain.repositories.ParkingSessionRepository;
import com.example.estapar.domain.repositories.ParkingSpotRepository;
import com.example.estapar.domain.repositories.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ParkingSessionService {

    @Autowired
    private ParkingSessionRepository sessionRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingSpotRepository spotRepository;

    @Autowired
    private ParkingSectorRepository sectorRepository;

    public PlateStatusResponse getPlateStatus(String licensePlate) {
        ParkingSession session = sessionRepository
                .findTopByVehicle_LicensePlateOrderByEntryTimeDesc(licensePlate)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma sessão encontrada para a placa " + licensePlate));

        PlateStatusResponse resp = new PlateStatusResponse();
        resp.setLicense_plate(session.getVehicle().getLicensePlate());
        resp.setEntry_time(session.getEntryTime().toString());

        LocalDateTime parkedSince = session.getParkedTime() != null
                ? session.getParkedTime()
                : session.getEntryTime();
        Duration durationParked = Duration.between(parkedSince, LocalDateTime.now());
        resp.setTime_parked(formatDuration(durationParked));

        if (session.getSpot() != null) {
            resp.setLat(session.getSpot().getLat());
            resp.setLng(session.getSpot().getLng());
        }

        resp.setPrice_until_now(calculateCurrentPrice(session, durationParked));

        return resp;
    }

    private Double calculateCurrentPrice(ParkingSession session, Duration duration) {
        double pricePerHour = session.getPriceCharged();
        return (duration.toMinutes() / 60.0) * pricePerHour;
    }

    private String formatDuration(Duration d) {
        return String.format("%02d:%02d", d.toHoursPart(), d.toMinutesPart());
    }

    public SpotStatusResponse getSpotStatus(Double lat, Double lng) {

        ParkingSpot spot = spotRepository.findByLatAndLng(lat, lng)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        ParkingSession session = sessionRepository.findTopBySpotAndExitTimeIsNull(spot).orElse(null);

        SpotStatusResponse resp = new SpotStatusResponse();
        resp.setOccupied(spot.getOccupied());

        if (session != null) {

            resp.setLicense_plate(session.getVehicle().getLicensePlate());
            resp.setEntry_time(session.getEntryTime().toString());

            LocalDateTime start = session.getParkedTime() != null ?
                    session.getParkedTime() : session.getEntryTime();

            Duration duration = Duration.between(
                    session.getParkedTime() != null ? session.getParkedTime() : session.getEntryTime(),
                    LocalDateTime.now());

            resp.setPrice_until_now(calculateCurrentPrice(session, duration));

            resp.setTime_parked(formatDuration(duration));
            resp.setPrice_until_now(calculateCurrentPrice(session, duration));
        } else {
            resp.setLicense_plate("");
            resp.setPrice_until_now(0.0);
            resp.setEntry_time(null);
            resp.setTime_parked(null);
        }
        return resp;
    }


    private double calcularPrecoDinamico(double precoBase, int ocupados, int capacidade) {
        double ocupacao = ((double)ocupados / capacidade) * 100;
        if (ocupacao < 25) return precoBase * 0.9;
        if (ocupacao < 50) return precoBase;
        if (ocupacao < 75) return precoBase * 1.1;
        return precoBase * 1.25;
    }

    @Transactional
    public void handleEntryEvent(WebhookEventDto event) {

        Vehicle vehicle = vehicleRepository.findByLicensePlate(event.getLicensePlate())
                .orElseGet(() -> vehicleRepository.save(new Vehicle(null, event.getLicensePlate())));

        ParkingSector sector = sectorRepository.findAll().stream()
                .filter(s -> sessionRepository.countByParkingSectorIdAndExitTimeIsNull(s.getId()) < s.getMaxCapacity())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhum setor disponível"));

        int ocupados = sessionRepository.countByParkingSectorIdAndExitTimeIsNull(sector.getId());
        double precoHora = calcularPrecoDinamico(sector.getBasePrice(), ocupados, sector.getMaxCapacity());

        ParkingSession session = new ParkingSession();
        session.setVehicle(vehicle);
        session.setParkingSector(sector);

        session.setEntryTime(event.getEntryTime() != null ? event.getEntryTime() : LocalDateTime.now());

        session.setPriceCharged(precoHora);
        sessionRepository.save(session);
    }


    @Transactional
    public void handleParkedEvent(WebhookEventDto event) {

        Vehicle vehicle = vehicleRepository.findByLicensePlate(event.getLicensePlate())
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado: " + event.getLicensePlate()));

        ParkingSession session = sessionRepository
                .findTopByVehicle_LicensePlateOrderByEntryTimeDesc(vehicle.getLicensePlate())
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada para o veículo: " + vehicle.getLicensePlate()));

        ParkingSpot spot = spotRepository.findByLatAndLng(event.getLat(), event.getLng())
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        session.setParkedTime(LocalDateTime.now());
        session.setSpot(spot);

        spot.setOccupied(true);
        spotRepository.save(spot);
        sessionRepository.save(session);
    }


    @Transactional
    public void handleExitEvent(WebhookEventDto event) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(event.getLicensePlate())
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado: " + event.getLicensePlate()));

        ParkingSession session = sessionRepository.findTopByVehicle_LicensePlateOrderByEntryTimeDesc(vehicle.getLicensePlate())
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada para o veículo: " + vehicle.getLicensePlate()));
        session.setExitTime(event.getExitTime());

        ParkingSpot spot = session.getSpot();
        if (spot != null) {
            spot.setOccupied(false);
            spotRepository.save(spot);
        }
        sessionRepository.save(session);
    }
}
