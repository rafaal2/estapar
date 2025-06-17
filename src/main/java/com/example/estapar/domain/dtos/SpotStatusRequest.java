// SpotStatusRequest.java
package com.example.estapar.domain.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpotStatusRequest {
    private Double lat;
    private Double lng;
}
