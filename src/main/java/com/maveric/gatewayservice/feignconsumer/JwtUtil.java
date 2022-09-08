package com.maveric.gatewayservice.feignconsumer;

import com.maveric.gatewayservice.dto.GateWayRequestDto;
import com.maveric.gatewayservice.dto.GateWayResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name="authentication-authorization-service")
@Service
public interface JwtUtil {
    @PostMapping("api/v1/auth/validate")
    public ResponseEntity<GateWayResponseDto> validateToken(@Valid @RequestBody GateWayRequestDto gateWayRequestDto);
}
