package com.saveit.bff.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableFeignClients
@SpringBootApplication
@EnableWebSecurity
@EnableMethodSecurity
public class BffNotesApplication {

    static void main(String[] args) {
        SpringApplication.run(BffNotesApplication.class, args);
    }

}
