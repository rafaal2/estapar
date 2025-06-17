package com.example.estapar.domain.dtos;

import com.example.estapar.domain.entities.ParkingSession;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSessionDTO {

    private Long id;

    @NotNull(message = "Veículo obrigatório")
    @JsonProperty("license_plate")
    private String licensePlate;

    @JsonProperty("price_until_now")
    private Double priceUntilNow;

    @JsonProperty("entry_time")
    private String entryTime;

    @JsonProperty("time_parked")
    private String timeParked;

    @JsonProperty("lat")
    private Double lat;

    @JsonProperty("lng")
    private Double lng;

    @JsonProperty("ocupied")
    private Boolean ocupied;

    public ParkingSessionDTO(ParkingSession entity) {
        this.id = entity.getId();
        this.licensePlate = entity.getVehicle().getLicensePlate();
        this.priceUntilNow = entity.getPriceCharged() != null ? entity.getPriceCharged() : 0.0;
        this.entryTime = entity.getEntryTime() != null ? entity.getEntryTime().toString() : null;
        this.timeParked = entity.getParkedTime() != null ? entity.getParkedTime().toString() : null;
        if (entity.getSpot() != null) {
            this.lat = entity.getSpot().getLat();
            this.lng = entity.getSpot().getLng();
            this.ocupied = entity.getSpot().getOccupied();
        }
    }
}
