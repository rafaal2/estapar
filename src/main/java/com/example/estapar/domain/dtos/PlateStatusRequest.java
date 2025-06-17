// PlateStatusRequest.java
package com.example.estapar.domain.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlateStatusRequest {
    private String license_plate;
}
