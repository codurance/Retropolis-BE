package com.codurance.retropolis.acceptance;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

public class BaseStepDefinition {

  protected final String TOKEN = "token";
  protected final HttpHeaders headers;
  private final String CLEAN_DB_SQL = "sql/cleanDb.sql";
  @Value("http://localhost:${server.port}")
  protected String url;
  protected JdbcTemplate jdbcTemplate;

  public BaseStepDefinition(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.headers = setHeaders();
  }

  private HttpHeaders setHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, TOKEN);
    return headers;
  }

  protected void cleanUp() throws SQLException {
    ScriptUtils.executeSqlScript(
        Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection(),
        new EncodedResource(new ClassPathResource(CLEAN_DB_SQL), StandardCharsets.UTF_8)
    );
  }
}
