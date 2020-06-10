package com.codurance.retropolis.repositories;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.exceptions.UserAlreadyUpvotedException;
import com.codurance.retropolis.repositories.mappers.CardMapper;
import java.sql.PreparedStatement;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresCardRepository implements CardRepository {

  private final String INSERT_CARD = "insert into cards (text, user_id, column_id, voters) values (?,?,?,?)";
  private final String SELECT_CARD = "select * from cards where id = ?";
  private final String DELETE_CARD = "delete from cards where id = ?";
  private final String UPDATE_CARD = "update cards set text = ? where id = ?";
  private final String ADD_VOTER = "update cards set voters = array_append(voters, ?::integer) where id = ?";
  private final String REMOVE_VOTER = "update cards set voters = array_remove(voters, ?::integer) where id = ?";
  private final JdbcTemplate jdbcTemplate;

  public PostgresCardRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public Card addCard(Card newCard) {
    KeyHolder key = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement statement = connection.prepareStatement(INSERT_CARD, new String[]{"id"});
      statement.setString(1, newCard.getText());
      statement.setLong(2, newCard.getUserId());
      statement.setLong(3, newCard.getColumnId());
      statement.setArray(4, connection.createArrayOf("int", new Integer[]{}));
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
  public Card updateText(Long cardId, String newText) {
    jdbcTemplate.update(UPDATE_CARD, newText, cardId);
    return getCard(cardId);
  }

  private Card getCard(Long id) {
    return jdbcTemplate.queryForObject(SELECT_CARD, new CardMapper(), id);
  }

  @Override
  public Card addUpvote(Long cardId, Long userId) {
    Card card = getCard(cardId);
    if (card.getVoters().contains(userId)) {
      throw new UserAlreadyUpvotedException();
    }

    jdbcTemplate.update(ADD_VOTER, userId, cardId);
    return getCard(cardId);
  }

  @Override
  public Card removeUpvote(Long cardId, Long userId) {
    jdbcTemplate.update(REMOVE_VOTER, userId, cardId);
    return getCard(cardId);
  }
}
