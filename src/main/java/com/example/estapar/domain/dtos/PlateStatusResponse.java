package com.example.estapar.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlateStatusResponse {
    private String license_plate;
    private Double price_until_now;
    private String entry_time;
    private String time_parked;
    private Double lat;
    private Double lng;
}

