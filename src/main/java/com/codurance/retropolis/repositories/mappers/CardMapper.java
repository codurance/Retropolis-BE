package com.codurance.retropolis.repositories.mappers;

import static java.util.stream.Collectors.toList;

import com.codurance.retropolis.entities.Card;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;

public class CardMapper implements RowMapper<Card> {

  @Override
  public Card mapRow(ResultSet resultSet, int i) throws SQLException {
    return new Card(
        resultSet.getLong("id"),
        resultSet.getString("text"),
        resultSet.getLong("column_id"),
        resultSet.getLong("user_id"),
        List.of(getVoters(resultSet)).stream().map(Long::valueOf).collect(toList())
    );
  }

  private Integer[] getVoters(ResultSet resultSet) throws SQLException {
    Array sqlArray = resultSet.getArray("voters");
    return (Integer[]) sqlArray.getArray();
  }
}