package com.example.javapimtg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// mvn spring-boot:run

@SpringBootApplication
public class JavapimtgApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavapimtgApplication.class, args);
        System.out.println("Application démarrée avec succès !");
    }
}
