package com.codurance.retropolis.web.responses;

import java.util.List;

public class BoardResponseObject {

  private Long id;
  private String title;
  private List<ColumnResponseObject> columns;

  public BoardResponseObject(Long id, String title, List<ColumnResponseObject> columns) {
    this.id = id;
    this.title = title;
    this.columns = columns;
  }

  public BoardResponseObject() {
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public List<ColumnResponseObject> getColumns() {
    return columns;
  }
}
