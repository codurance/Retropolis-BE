package com.codurance.retropolis.entities;

import java.util.List;

public class Column {

  private Long id;
  private Long boardId;
  private String title;
  private List<Card> cards;

  public Column() {
  }

  public Column(String title, List<Card> cards) {
    this.title = title;
    this.cards = cards;
  }

  public Column(Long id, String title, Long boardId) {
    this.id = id;
    this.title = title;
    this.boardId = boardId;
  }

  public Column(Long id, String title, List<Card> cards) {
    this.id = id;
    this.title = title;
    this.cards = cards;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public List<Card> getCards() {
    return cards;
  }

  public void setCards(List<Card> columnCards) {
    this.cards = columnCards;
  }
}
