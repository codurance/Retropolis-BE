package com.codurance.retropolis.config.database;

import com.codurance.retropolis.config.Environment;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
@Profile(Environment.TEST)
public class DatabaseTestConfig {

  private static DataSource dataSource;
  private static PostgreSQLContainer postgreSQLContainer;

  @Bean
  public DataSource dataSource() {
    postgreSQLContainer = new PostgreSQLContainer("postgres");
    postgreSQLContainer.start();
    dataSource = DataSourceBuilder.create()
        .url(postgreSQLContainer.getJdbcUrl())
        .username(postgreSQLContainer.getUsername())
        .password(postgreSQLContainer.getPassword())
        .driverClassName("org.postgresql.Driver")
        .build();

    return dataSource;
  }

}
