package com.example.estapar.domain.dtos;

import com.example.estapar.domain.entities.Vehicle;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {

    private Long id;

    @NotBlank(message = "Placa obrigatória")
    private String licensePlate;

    public VehicleDTO(Vehicle entity) {
        this.id = entity.getId();
        this.licensePlate = entity.getLicensePlate();
    }
}
