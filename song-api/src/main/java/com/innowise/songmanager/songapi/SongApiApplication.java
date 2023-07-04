package com.innowise.songmanager.songapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.innowise.songmanager.songapi", "com.innowise.songmanager.contractapi"})
@EnableFeignClients
@EnableDiscoveryClient
public class SongApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongApiApplication.class, args);
    }

}
