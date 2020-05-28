package com.codurance.retropolis.repositories.mappers;

import com.codurance.retropolis.models.Card;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;

public class CardMapper implements RowMapper<Card> {

  @Override
  public Card mapRow(ResultSet resultSet, int i) throws SQLException {
      return new Card(
        resultSet.getLong("id"),
        resultSet.getString("text"),
        resultSet.getLong("column_id"),
        resultSet.getString("username"),
        List.of(getVoters(resultSet))
    );
  }

    private String[] getVoters(ResultSet resultSet) throws SQLException {
        Array sqlArray = resultSet.getArray("voters");
        return (String[]) sqlArray.getArray();
    }
}