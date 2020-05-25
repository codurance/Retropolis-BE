package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.models.util.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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
        return jdbcTemplate.queryForObject("select * from boards where id = ?", new Object[] {id}, new BoardMapper());
    }

    @Override
    public void addCard(Card card) {

    }
}
