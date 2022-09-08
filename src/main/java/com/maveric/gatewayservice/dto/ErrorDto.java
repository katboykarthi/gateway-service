package com.maveric.gatewayservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ErrorDto {
    String code;
    String message;
}
