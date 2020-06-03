package com.codurance.retropolis.config.database;

import com.codurance.retropolis.config.Environment;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@Profile({Environment.PROD, Environment.DEV})
public class DatabaseConfig {

  @Value("${datasourceUrl}")
  private String dataSourceUrl;

  @Value("${datasourceUsername}")
  private String username;

  @Value("${datasourcePass}")
  private String password;

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl(dataSourceUrl);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    return dataSource;
  }
}
