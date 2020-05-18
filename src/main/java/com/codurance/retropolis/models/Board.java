package com.codurance.retropolis.models;

import java.util.List;

public class Board {
    private List<Column> columns;

    public Board(List<Column> columns) {
        this.columns = columns;
    }

    public Board() {
    }

    public List<Column> getColumns() {
        return columns;
    }
}
