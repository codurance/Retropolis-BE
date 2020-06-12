package com.codurance.retropolis.config.database;

import com.codurance.retropolis.config.Environment;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
@Profile(Environment.TEST)
public class DatabaseTestConfig {

  private static PostgreSQLContainer postgreSQLContainer;

  @Bean
  public DataSource dataSource() {
    postgreSQLContainer = new PostgreSQLContainer("postgres");
    postgreSQLContainer.start();
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl(postgreSQLContainer.getJdbcUrl());
    dataSource.setUsername(postgreSQLContainer.getUsername());
    dataSource.setPassword(postgreSQLContainer.getPassword());
    return dataSource;
  }

}
