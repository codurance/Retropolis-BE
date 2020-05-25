package com.codurance.retropolis.repositories.util;

import com.codurance.retropolis.models.Card;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CardMapper implements RowMapper<Card> {
    @Override
    public Card mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Card(
            resultSet.getInt("id"),
            resultSet.getString("text"),
            resultSet.getInt("column_id"),
            resultSet.getString("username")
        );
    }
}