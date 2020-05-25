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

    public static final String INSERT_CARD = "insert into cards (text, username, column_id) values (?,?,?)";
    public static final String SELECT_CARD = "select * from cards where id = ?";
    private final JdbcTemplate jdbcTemplate;

    public PostgresCardRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Card insert(Card newCard) {
        KeyHolder key = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_CARD, new String[]{"id"});
            statement.setString(1, newCard.getText());
            statement.setString(2, newCard.getUserName());
            statement.setInt(3, newCard.getColumnId());
            return statement;
        }, key);

        Integer id = key.getKey().intValue();
        return getCard(id);
    }

    private Card getCard(Integer id) {
        return jdbcTemplate.queryForObject(SELECT_CARD, new CardMapper(), id);
    }
}
