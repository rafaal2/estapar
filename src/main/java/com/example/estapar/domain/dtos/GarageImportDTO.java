package com.example.estapar.domain.dtos;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GarageImportDTO {
    private List<ParkingSectorDTO> garage;
    private List<ParkingSpotDTO> spots;
}