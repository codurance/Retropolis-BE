package com.codurance.retropolis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@ActiveProfiles(Environment.PROD)
public class Retropolis {

    public static void main(String[] args) {
        SpringApplication.run(Retropolis.class, args);
    }

}