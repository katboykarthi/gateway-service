package com.maveric.gatewayservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    @Value("${authentication-authorization-service.path}")
    public String authServicePath;
    @Value("${authentication-authorization-service.uri}")
    public String authServiceUri;
    @Value("${user-service.path}")
    public String userServicePath;
    @Value("${user-service.uri}")
    public String userServiceUri;

    @Value("${account-service.path}")
    public String accountServicePath;

    @Value("${account-service.uri}")
    public String accountServiceUri;

    @Value("${balance-service.path}")
    public String balanceServicePath;

    @Value("${balance-service.uri}")
    public String balanceServiceUri;

    @Value("${transaction-service.path}")
    public String transactionServicePath;

    @Value("${transaction-service.uri}")
    public String transactionServiceUri;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authentication-authorization-service",
                        r -> r.path(authServicePath,"/api/v1/hello").uri(authServiceUri))
                .route("user-service",
                        r -> r.path(userServicePath).uri(userServiceUri))
                .route("account-service",
                        r -> r.path(accountServicePath).uri(accountServiceUri))
                .route("balance-service",
                        r -> r.path(balanceServicePath).uri(balanceServiceUri))
                .route("transaction-service",
                        r -> r.path(transactionServicePath).uri(transactionServiceUri))
                .build();
    }
}
