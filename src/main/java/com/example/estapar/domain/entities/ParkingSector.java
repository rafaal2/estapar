package com.example.estapar.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "parking_sector")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sector;

    private Double basePrice;

    private Integer maxCapacity;

    private LocalTime openHour;

    private LocalTime closeHour;

    private Integer durationLimitMinutes;
}
