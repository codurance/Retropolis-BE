package com.codurance.retropolis.repositories;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.repositories.mappers.BoardMapper;
import com.codurance.retropolis.repositories.mappers.CardMapper;
import com.codurance.retropolis.repositories.mappers.ColumnMapper;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresBoardRepository implements BoardRepository {

  private final String SELECT_BOARD = "select * from boards where id = ?";
  private final String SELECT_COLUMNS = "select * from columns where board_id = ?";
  private final String SELECT_CARDS = "select * from cards where column_id = ? ORDER BY id ASC";
  private final String INSERT_USER_TO_BOARD = "insert into users_boards (user_id,board_id) values (?,?)";
  private final String SELECT_USERS_BOARDS = "select boards.title, boards.id from boards inner join "
      + "users_boards on boards.id = users_boards.board_id where users_boards.user_id = ? "
      + "ORDER BY boards.id ASC";
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
    jdbcTemplate.update(INSERT_USER_TO_BOARD, userId, boardId);
  }

  @Override
  public List<Board> getUsersBoards(Long userId) {
    return jdbcTemplate.query(SELECT_USERS_BOARDS, new Object[]{userId}, new BoardMapper());
  }
}
