package com.maveric.gatewayservice.filter;

import com.maveric.gatewayservice.dto.GateWayRequestDto;
import com.maveric.gatewayservice.dto.GateWayResponseDto;
import com.maveric.gatewayservice.feignconsumer.JwtUtil;
import com.maveric.gatewayservice.util.JwtAuthUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RefreshScope
@Component
public class JwtAuthFilter implements GatewayFilter {

    @Autowired
    private RouterValidator routerValidator;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtAuthUtil jwtAuthUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request))
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

            final String token = this.getAuthHeader(request);
            System.out.println("Token->"+token);
            String tokenn = token.substring(7);
            System.out.println("Token->"+tokenn);
            GateWayRequestDto gateWayRequestDto = new GateWayRequestDto(token);

            try {
                GateWayResponseDto gateWayResponseDto = validateToken(tokenn);
                System.out.println("gateWayResponseDto->"+gateWayResponseDto.isResponse()+"--"+gateWayResponseDto.getClaims());
                if (!gateWayResponseDto.isResponse())
                    return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

                this.populateRequestWithHeaders(exchange, gateWayResponseDto.getClaims());
            }
            catch(Exception e)
            {
                System.out.println("Exception during validation of token-"+e);
            }
        }
        return chain.filter(exchange);
    }


    /*PRIVATE*/

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, Claims claims) {
        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("sub")))
                .build();
    }

    private GateWayResponseDto validateToken(String token) {
        try {
            //ResponseEntity<GateWayResponseDto> responseEntity = jwtUtil.validateToken(gateWayRequestDto);
            GateWayResponseDto responseEntity = jwtAuthUtil.validateToken(token);
            /*HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<GateWayRequestDto> entity = new HttpEntity<GateWayRequestDto>(gateWayRequestDto,headers);
            GateWayResponseDto responseEntity = restTemplate.exchange("http://localhost:3000/api/v1/auth/validate", HttpMethod.POST, entity, GateWayResponseDto.class).getBody(); */
            System.out.println("Success Validation of token->"+responseEntity.isResponse());
            return responseEntity;
        }
        catch (Exception e)
        {
            System.out.println("Exception with feign !! -> "+e);
            return new GateWayResponseDto();
        }

    }


}