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
    JdbcTemplate jdbcTemplate;

    @Autowired
    public PostgresBoardRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Board getBoard(Integer id) {
        Board board = jdbcTemplate.queryForObject("select * from boards where id = ?", new Object[]{id}, new BoardMapper());
        if (board != null) {
            List<Column> columns = jdbcTemplate.query("select * from columns where board_id = ?", new Object[]{id}, new ColumnMapper());
            board.setColumns(columns);
            columns.forEach((column -> {
                List<Card> columnCards = jdbcTemplate.query("select * from cards where column_id = ?", new Object[]{column.getId()}, new CardMapper());
                column.setCards(columnCards);
            }));
        }
        return board;
    }

    @Override
    public void addCard(Card card) {

    }
}
