package com.example.estapar.domain.entities;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "spot_id")
    private ParkingSpot spot;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private ParkingSector parkingSector;

    private LocalDateTime entryTime;

    private LocalDateTime parkedTime;

    private LocalDateTime exitTime;

    private Double priceCharged;
}
