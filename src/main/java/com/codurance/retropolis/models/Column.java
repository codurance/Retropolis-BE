package com.codurance.retropolis.models;

import java.util.List;

public class Column {

  private int id;
  private int board_id;
  private String title;
  private List<Card> cards;

  public Column() {
  }

  public Column (int id, String title, int board_id) {
    this.id = id;
    this.title = title;
    this.board_id = board_id;
  }

  public Column(int id, String title, List<Card> cards) {
    this.id = id;
    this.title = title;
    this.cards = cards;
  }

  public int getId() {
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
