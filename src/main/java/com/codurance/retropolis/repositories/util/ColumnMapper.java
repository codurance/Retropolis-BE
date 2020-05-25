package com.codurance.retropolis.repositories.util;

import com.codurance.retropolis.models.Column;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnMapper implements RowMapper<Column> {
    @Override
    public Column mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Column(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getInt("board_id")
        );
    }
}
