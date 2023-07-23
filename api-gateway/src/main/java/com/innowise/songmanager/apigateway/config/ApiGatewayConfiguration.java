package com.innowise.songmanager.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("file_route", route -> route.path("/songs/file/**")
                .filters(filter -> filter
                    .rewritePath("songs/file", "files"))
                .uri("lb://file-api"))
            .route("song_route", route -> route.path("/songs/**")
                .uri("lb://song-api"))
            .build();
    }
}
