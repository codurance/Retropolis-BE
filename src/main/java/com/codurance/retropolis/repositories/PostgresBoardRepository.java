package com.codurance.retropolis.repositories;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.repositories.mappers.BoardMapper;
import com.codurance.retropolis.repositories.mappers.CardMapper;
import com.codurance.retropolis.repositories.mappers.ColumnMapper;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresBoardRepository implements BoardRepository {

  private final String INSERT_COLUMN = "insert into columns(title, board_id) values(?, ?)";
  private final String INSERT_BOARD = "insert into boards(title) values(?)";
  private final String SELECT_BOARD = "select * from boards where id = ?";
  private final String SELECT_COLUMNS = "select * from columns where board_id = ?";
  private final String SELECT_CARDS = "select * from cards where column_id = ? ORDER BY id ASC";
  private final String INSERT_USER_TO_BOARD = "insert into users_boards (user_id,board_id) values (?,?)";
  private final String SELECT_USER_FROM_BOARD = "SELECT EXISTS(SELECT FROM users_boards WHERE user_id=? and board_id = ?)";
  private final JdbcTemplate jdbcTemplate;

  public PostgresBoardRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public Board getBoard(Long id) {
    Board board = jdbcTemplate.queryForObject(SELECT_BOARD, new Object[]{id}, new BoardMapper());
    if (board != null) {
      List<Column> columns = jdbcTemplate.query(SELECT_COLUMNS, new Object[]{id}, new ColumnMapper());
      board.setColumns(columns);
      columns.forEach((column -> {
        List<Card> columnCards = jdbcTemplate.query(SELECT_CARDS, new Object[]{column.getId()}, new CardMapper());
        column.setCards(columnCards);
      }));
    }
    return board;
  }

  @Override
  public void addToBoard(Long userId, Long boardId) {
    Boolean userExistsOnBoard = jdbcTemplate.queryForObject(SELECT_USER_FROM_BOARD, Boolean.class, userId, boardId);
    if (userExistsOnBoard != null && !userExistsOnBoard) {
      jdbcTemplate.update(INSERT_USER_TO_BOARD, userId, boardId);
    }
  }

  @Override
  public Board insert(Board board) {
    KeyHolder key = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement statement = connection.prepareStatement(INSERT_BOARD, new String[]{"id"});
      statement.setString(1, board.getTitle());
      return statement;
    }, key);

    String boardId = key.getKey().toString();

    for (Column column : board.getColumns()) {
      jdbcTemplate.update(connection -> {
        PreparedStatement statement = connection.prepareStatement(INSERT_COLUMN, new String[]{"id"});
        statement.setString(1, column.getTitle());
        statement.setString(2, boardId);

        return statement;
      });
    }

    return getBoard(Long.parseLong(boardId));
  }
}
