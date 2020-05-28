package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.models.Column;
import com.codurance.retropolis.repositories.mappers.BoardMapper;
import com.codurance.retropolis.repositories.mappers.CardMapper;
import com.codurance.retropolis.repositories.mappers.ColumnMapper;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresBoardRepository implements BoardRepository {

  private final String SELECT_BOARD = "select * from boards where id = ?";
  private final String SELECT_COLUMNS = "select * from columns where board_id = ?";
  private final String SELECT_CARDS = "select * from cards where column_id = ?";
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public PostgresBoardRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public Board getBoard(Integer id) {
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
}
