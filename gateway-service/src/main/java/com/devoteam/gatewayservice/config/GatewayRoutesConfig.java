package com.devoteam.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayRoutesConfig {

  @Bean
  public RouteLocator routeLocator(RouteLocatorBuilder builder) {
    return builder.routes()
        .route(r -> r.path("/access-control-service/v3/api-docs")
            .and().method(HttpMethod.GET)
            .uri("lb://access-control-service"))
        .route(r -> r.path("/demo/v3/api-docs")
            .and().method(HttpMethod.GET)
            .uri("lb://demo"))
        .build();
  }
}

