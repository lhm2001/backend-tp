package com.tesis.proyecto_tesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ProyectoTesisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProyectoTesisApplication.class, args);
    }

}
