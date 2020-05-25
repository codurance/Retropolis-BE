package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.models.Column;
import com.codurance.retropolis.repositories.util.BoardMapper;
import com.codurance.retropolis.repositories.util.CardMapper;
import com.codurance.retropolis.repositories.util.ColumnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Primary
@Repository
public class PostgresBoardRepository implements BoardRepository {
    public static final String SELECT_BOARD = "select * from boards where id = ?";
    public static final String SELECT_COLUMNS = "select * from columns where board_id = ?";
    public static final String SELECT_CARDS = "select * from cards where column_id = ?";
    JdbcTemplate jdbcTemplate;

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

    @Override
    public void addCard(Card card) {

    }
}
