package com.codurance.retropolis.entities;

import java.util.ArrayList;
import java.util.List;

public class Column {

  private Long id;
  private ColumnType title;
  private List<Card> cards = new ArrayList<>();

  public Column() {
  }

  public Column(ColumnType title) {
    this.title = title;
  }

  public Column(Long id, ColumnType title){
    this.id = id;
    this.title = title;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title.getTitle();
  }

  public List<Card> getCards() {
    return cards;
  }

  public void setCards(List<Card> columnCards) {
    this.cards = columnCards;
  }
}
