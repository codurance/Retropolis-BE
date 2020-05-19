package com.codurance.retropolis.requests;

public class NewCardRequestObject {

  private String text;
  private int columnId;

  public NewCardRequestObject() {
  }

  public NewCardRequestObject(String text, int columnId) {
    this.text = text;
    this.columnId = columnId;
  }

  public String getText() {
    return text;
  }

  public int getColumnId() {
    return columnId;
  }
}
