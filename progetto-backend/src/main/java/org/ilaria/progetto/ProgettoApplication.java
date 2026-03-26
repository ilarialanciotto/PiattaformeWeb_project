package org.ilaria.progetto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableMethodSecurity
public class ProgettoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProgettoApplication.class, args);
    }
}

