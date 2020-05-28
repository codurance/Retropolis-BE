package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.repositories.mappers.CardMapper;
import java.sql.Array;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class PostgresCardRepository implements CardRepository {

  private final String INSERT_CARD = "insert into cards (text, username, column_id, voters) values (?,?,?,?)";
  private final String SELECT_CARD = "select * from cards where id = ?";
  private final String DELETE_CARD = "delete from cards where id = ?";
  private final String UPDATE_CARD = "update cards set text = ? where id = ?";
  private final String ADD_VOTER = "update cards set voters = array_append(voters, ?::varchar) where id = ?";
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
      statement.setString(2, newCard.getUsername());
      statement.setLong(3, newCard.getColumnId());
      statement.setArray(4, connection.createArrayOf("varchar", new String[]{}));
      return statement;
    }, key);

    Long id = Objects.requireNonNull(key.getKey()).longValue();
    return getCard(id);
  }

  @Override
  public void delete(Long cardId) {
    jdbcTemplate.update(DELETE_CARD, cardId);
  }

  @Override
  public Card update(Long cardId, String newText) {
    jdbcTemplate.update(UPDATE_CARD, newText, cardId);
    return getCard(cardId);
  }

  private Card getCard(Long id) {
    return jdbcTemplate.queryForObject(SELECT_CARD, new CardMapper(), id);
  }

  @Override
  public Card addVoter(Long cardId, String username) {
    jdbcTemplate.update(ADD_VOTER, username, cardId);
    return getCard(cardId);
  }
}
