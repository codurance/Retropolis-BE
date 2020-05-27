package com.codurance.retropolis.acceptance;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class BaseStepDefinition {

    protected JdbcTemplate jdbcTemplate;
    public BaseStepDefinition(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
