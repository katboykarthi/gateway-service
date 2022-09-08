package com.maveric.gatewayservice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GateWayRequestDto {
    String token;
}
