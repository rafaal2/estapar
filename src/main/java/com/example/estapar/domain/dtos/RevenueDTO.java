package com.example.estapar.domain.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueDTO {
    private double amount;
    private String currency;
    private String timestamp;
}
