package com.example.estapar.domain.dtos;

import com.example.estapar.domain.entities.ParkingSector;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSectorDTO {
        private Long id;

        @JsonProperty("sector")
        private String sector;

        @JsonProperty("base_price")
        private Double basePrice;

        @JsonProperty("max_capacity")
        private Integer maxCapacity;

        @JsonProperty("open_hour")
        private String openHour;

        @JsonProperty("close_hour")
        private String closeHour;

        @JsonProperty("duration_limit_minutes")
        private Integer durationLimitMinutes;


    public ParkingSectorDTO(ParkingSector entity) {
        this.id = entity.getId();
        this.sector = entity.getSector();
        this.basePrice = entity.getBasePrice();
        this.maxCapacity = entity.getMaxCapacity();
        this.openHour = entity.getOpenHour().toString();
        this.closeHour = entity.getCloseHour().toString();
        this.durationLimitMinutes = entity.getDurationLimitMinutes();
    }
}
