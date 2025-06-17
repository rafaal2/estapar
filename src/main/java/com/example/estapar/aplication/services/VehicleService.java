package com.example.estapar.aplication.services;

import com.example.estapar.domain.dtos.VehicleDTO;
import com.example.estapar.domain.entities.Vehicle;
import com.example.estapar.domain.repositories.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Transactional(readOnly = true)
    public VehicleDTO findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado: " + id));
        return new VehicleDTO(vehicle);
    }

    @Transactional(readOnly = true)
    public VehicleDTO findByLicensePlate(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado para a placa: " + licensePlate));
        return new VehicleDTO(vehicle);
    }

    @Transactional
    public VehicleDTO save(VehicleDTO dto) {
        Vehicle entity = new Vehicle();
        copyDtoToEntity(dto, entity);
        entity = vehicleRepository.save(entity);
        return new VehicleDTO(entity);
    }

    @Transactional
    public VehicleDTO update(Long id, VehicleDTO dto) {
        Vehicle entity = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado: " + id));
        copyDtoToEntity(dto, entity);
        entity = vehicleRepository.save(entity);
        return new VehicleDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new EntityNotFoundException("Veículo não encontrado: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    private void copyDtoToEntity(VehicleDTO dto, Vehicle entity) {
        entity.setLicensePlate(dto.getLicensePlate());
    }
}
