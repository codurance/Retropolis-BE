package com.codurance.retropolis.entities;

import java.util.List;

public class Card {

  private Long id;
  private Long columnId;
  private String text;
  private Long userId;
  private List<Long> voters;

  public Card() {
  }

  public Card(String text, Long columnId, Long userId, List<Long> voters) {
    this.text = text;
    this.columnId = columnId;
    this.userId = userId;
    this.voters = voters;
  }

  public Card(Long id, String text, Long columnId, Long userId, List<Long> voters) {
    this.id = id;
    this.text = text;
    this.columnId = columnId;
    this.userId = userId;
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

  public Long getUserId() {
    return userId;
  }

  public List<Long> getVoters() {
    return voters;
  }
}
