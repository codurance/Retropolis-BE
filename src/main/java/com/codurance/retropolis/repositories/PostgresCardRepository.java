package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.repositories.util.CardMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

@Repository
public class PostgresCardRepository implements CardRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostgresCardRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Card insert(Card newCard) {
        KeyHolder key = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {

            PreparedStatement statement = connection.prepareStatement(
                    "insert into cards (text, username, column_id) values (?,?,?)",
                    new String[]{"id"});
            statement.setString(1, newCard.getText());
            statement.setString(2, newCard.getUserName());
            statement.setInt(3, newCard.getColumnId());
            return statement;

        }, key);


        return getCard(key.getKey().intValue());
    }

    private Card getCard(Integer id) {
        return jdbcTemplate.queryForObject("select * from cards where column_id = ?",
                new Object[]{id},
                new CardMapper());
    }
}
