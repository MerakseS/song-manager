package com.innowise.songmanager.songapi.config;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

@Configuration
public class Resilience4JConfig {

    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> defaultConfig() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig
            .from(CircuitBreakerConfig.ofDefaults())
            .slidingWindowSize(3)
            .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
            .circuitBreakerConfig(circuitBreakerConfig)
            .build());
    }
}
