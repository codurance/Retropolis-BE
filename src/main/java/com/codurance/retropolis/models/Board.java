package com.codurance.retropolis.models;

import java.util.List;

public class Board {
    private List<Column> columns;

    public Board() {
    }

    public Board(List<Column> columns) {
        this.columns = columns;
    }

    public List<Column> getColumns() {
        return columns;
    }
}
