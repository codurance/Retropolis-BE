package com.codurance.retropolis.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewCardRequestObject {

  @NotNull(message = "Text cannot be empty")
  @Size(min = 1, message = "Text must not be less than 1 characters")
  private String text;

  @NotNull(message = "Column id cannot be empty")
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
