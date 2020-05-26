package com.codurance.retropolis.models;

public class Card {

  private Long id;
  private Long columnId;
  private String text;
  private String username;

  public Card() {
  }

  public Card(Long id, String text, Long columnId, String username) {
    this.id = id;
    this.text = text;
    this.columnId = columnId;
    this.username = username;
  }

  public Card(String text, Long columnId, String username) {
    this.text = text;
    this.columnId = columnId;
    this.username = username;
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

  public String getUsername() {
    return username;
  }
}
