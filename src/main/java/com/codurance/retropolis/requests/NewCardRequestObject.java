package com.codurance.retropolis.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewCardRequestObject {

  @NotNull
  @Size(min = 1, message = "Text must not be less than 1 character")
  private String text;

  @NotNull(message = "Column id cannot be empty")
  private Integer columnId;

  private String userName;

  public NewCardRequestObject() {
  }

  public NewCardRequestObject(String text, Integer columnId, String userName) {
    this.text = text;
    this.columnId = columnId;
    this.userName = userName;
  }

  public String getText() {
    return text;
  }

  public int getColumnId() {
    return columnId;
  }

  public String getUserName() {
    return userName;
  }
}
