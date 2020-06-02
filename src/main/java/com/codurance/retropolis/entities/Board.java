package com.codurance.retropolis.entities;

import java.util.List;

public class Board {

  private Long id;
  private String title;
  private List<Column> columns;

  public Board() {
  }

  public Board(Long id, String title) {
    this.id = id;
    this.title = title;
  }

  public Board(Long id, String title, List<Column> columns) {
    this.id = id;
    this.title = title;
    this.columns = columns;
  }

  public String getTitle() {
    return title;
  }

  public List<Column> getColumns() {
    return columns;
  }

  public Long getId() {
    return id;
  }

  public void setColumns(List<Column> columns) {
    this.columns = columns;
  }
}
