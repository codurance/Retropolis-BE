package com.codurance.retropolis;

import com.codurance.retropolis.config.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@ActiveProfiles(Environment.PROD)
public class Retropolis {

  public static void main(String[] args) {
    SpringApplication.run(Retropolis.class, args);
  }

}