package com.innowise.songapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SongApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongApiApplication.class, args);
    }

}
