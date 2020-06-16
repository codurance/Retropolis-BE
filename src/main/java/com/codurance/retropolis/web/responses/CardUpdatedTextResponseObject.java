package com.codurance.retropolis.web.responses;

public class CardUpdatedTextResponseObject {

  private String text;
  private Long id;
  private Long columnId;

  public CardUpdatedTextResponseObject() {
  }

  public CardUpdatedTextResponseObject(Long id, String text, Long columnId) {
    this.text = text;
    this.id = id;
    this.columnId = columnId;
  }

  public String getText() {
    return text;
  }

  public Long getId() {
    return id;
  }

  public Long getColumnId() {
    return columnId;
  }
}
