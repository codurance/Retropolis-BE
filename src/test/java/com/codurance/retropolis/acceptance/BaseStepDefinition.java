package com.codurance.retropolis.acceptance;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;

public class BaseStepDefinition {

    private final String CLEAN_DB_SQL = "sql/cleanDb.sql";
    protected JdbcTemplate jdbcTemplate;

    public BaseStepDefinition(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected void cleanUp() throws SQLException {
        ScriptUtils.executeSqlScript(
                Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection(),
                new EncodedResource(new ClassPathResource(CLEAN_DB_SQL), StandardCharsets.UTF_8)
        );
    }
}
