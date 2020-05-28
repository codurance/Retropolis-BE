package com.codurance.retropolis.repositories.mappers;

import com.codurance.retropolis.models.Card;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;

public class CardMapper implements RowMapper<Card> {

  @Override
  public Card mapRow(ResultSet resultSet, int i) throws SQLException {
      System.out.println(resultSet.getArray("voters"));
      return new Card(
        resultSet.getLong("id"),
        resultSet.getString("text"),
        resultSet.getLong("column_id"),
        resultSet.getString("username")
    );
  }
}