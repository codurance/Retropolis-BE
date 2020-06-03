package com.codurance.retropolis.entities;

import java.util.List;

public class Column {

  private Long id;
  private Long boardId;
  private ColumnType title;
  private List<Card> cards;

  public Column() {
  }

  public Column(ColumnType title, List<Card> cards) {
    this.title = title;
    this.cards = cards;
  }

  public Column(Long id, ColumnType title, Long boardId){
    this.id = id;
    this.title = title;
    this.boardId = boardId;
  }

  public Column(Long id, ColumnType title, List<Card> cards) {
    this.id = id;
    this.title = title;
    this.cards = cards;
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
