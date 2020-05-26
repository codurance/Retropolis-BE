package com.codurance.retropolis.models;

public class Card {

  private Long id;
  private Long columnId;
  private String text;
  private String userName;

  public Card() {
  }

  public Card(Long id, String text, Long columnId, String userName) {
    this.id = id;
    this.text = text;
    this.columnId = columnId;
    this.userName = userName;
  }

  public Card(String text, Long columnId, String userName) {
    this.text = text;
    this.columnId = columnId;
    this.userName = userName;
  }

  public String getText() {
    return text;
  }

  public Long getColumnId() {
    return columnId;
  }

  public Long getId() {
    return id;
  }

  public String getUserName() {
    return userName;
  }
}
