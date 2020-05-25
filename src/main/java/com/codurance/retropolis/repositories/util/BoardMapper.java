package com.codurance.retropolis.repositories.util;

import com.codurance.retropolis.models.Board;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardMapper implements RowMapper<Board> {
    @Override
    public Board mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Board(
                resultSet.getInt("id"),
                resultSet.getString("title")
        );
    }
}
