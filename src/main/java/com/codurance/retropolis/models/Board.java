package com.codurance.retropolis.models;

import java.util.List;

public class Board {

    private Integer id;
    private String title;
    private List<Column> columns;

    public Board() {
    }

    public Board(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Board(Integer id, String title, List<Column> columns) {
        this.id = id;
        this.title = title;
        this.columns = columns;
    }

    public Board(List<Column> columns) {
        this.columns = columns;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
