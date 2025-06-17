package com.example.estapar.domain.dtos;

import com.example.estapar.domain.entities.ParkingSpot;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpotDTO {

    private Long id;

    @JsonProperty("sector")
    private String sectorName;

    private Double lat;

    private Double lng;

    private Boolean occupied;

    public ParkingSpotDTO(ParkingSpot entity) {
        this.id = entity.getId();
        this.lat = entity.getLat();
        this.lng = entity.getLng();
        this.occupied = entity.getOccupied();
    }
}
