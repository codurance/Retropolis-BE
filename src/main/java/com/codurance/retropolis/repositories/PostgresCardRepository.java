package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Card;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PostgresCardRepository implements CardRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostgresCardRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Card insert(Card newCard) {
        jdbcTemplate.update("insert into cards (text, username, column_id) values (?,?,?)",
                newCard.getText(),
                newCard.getUserName(),
                newCard.getColumnId());

        return null;
    }
}
