package com.codurance.retropolis.entities;

import java.util.List;

public class Card {

  private Long id;
  private Long columnId;
  private String text;
  private String username;
  private List<String> voters;

  public Card() {
  }

  public Card(String text, Long columnId, String username, List<String> voters) {
    this.text = text;
    this.columnId = columnId;
    this.username = username;
    this.voters = voters;
  }

  public Card(Long id, String text, Long columnId, String username, List<String> voters) {
    this.id = id;
    this.text = text;
    this.columnId = columnId;
    this.username = username;
    this.voters = voters;
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
