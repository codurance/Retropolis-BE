package com.codurance.retropolis.repositories.mappers;

import com.codurance.retropolis.entities.Board;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class BoardMapper implements RowMapper<Board> {
    @Override
    public Board mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Board(
            resultSet.getLong("id"),
            resultSet.getString("title")
        );
    }
}
