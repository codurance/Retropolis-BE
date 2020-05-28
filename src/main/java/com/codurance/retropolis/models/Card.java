package com.codurance.retropolis.models;

import java.util.List;

public class Card {

  private Long id;
  private Long columnId;
  private String text;
  private String username;
  private List<String> voters;

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

  public List<String> getVoters() {
    return voters;
  }
}
