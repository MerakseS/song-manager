package com.innowise.songmanager.authapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.cloudyrock.spring.v5.EnableMongock;

@SpringBootApplication
@EnableMongock
public class AuthApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApiApplication.class, args);
    }
}
