package com.maveric.gatewayservice.util;

import com.maveric.gatewayservice.dto.GateWayResponseDto;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public GateWayResponseDto validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return new GateWayResponseDto(true,extractAllClaims(token));
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return new GateWayResponseDto(false,null);
    }
}
