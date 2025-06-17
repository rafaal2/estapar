package com.example.estapar.domain.entities;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "parking_spot")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double lat;

    private Double lng;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private ParkingSector parkingSector;

    private Boolean occupied = false;
}
