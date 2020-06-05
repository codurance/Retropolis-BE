package com.codurance.retropolis.repositories.mappers;

import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.entities.ColumnType;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ColumnMapper implements RowMapper<Column> {
    @Override
    public Column mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Column(
            resultSet.getLong("id"),
            ColumnType.of(resultSet.getString("title"))
        );
    }
}
