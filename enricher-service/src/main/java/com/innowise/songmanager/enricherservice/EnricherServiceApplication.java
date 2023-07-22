package com.innowise.songmanager.enricherservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.innowise.songmanager.enricherservice", "com.innowise.songmanager.contractapi"})
@EnableDiscoveryClient
@EnableScheduling
public class EnricherServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnricherServiceApplication.class, args);
    }

}
