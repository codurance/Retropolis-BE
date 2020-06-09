package com.codurance.retropolis.responses;

import java.util.List;

public class ColumnResponseObject {

  private Long id;
  private String title;
  private List<CardResponseObject> cards;

  public ColumnResponseObject(Long id, String title, List<CardResponseObject> cards) {
    this.id = id;
    this.title = title;
    this.cards = cards;
  }

  public ColumnResponseObject() {
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public List<CardResponseObject> getCards() {
    return cards;
  }
}
