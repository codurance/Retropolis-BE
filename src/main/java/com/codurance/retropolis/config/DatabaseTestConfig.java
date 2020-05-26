package com.codurance.retropolis.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@Profile(Environment.TEST)
public class DatabaseTestConfig {

  @Value("${datasourceDevUrl}")
  private String dataSourceUrl;

  @Value("${datasourceDevUsername}")
  private String username;

  @Value("${datasourceDevPass}")
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
