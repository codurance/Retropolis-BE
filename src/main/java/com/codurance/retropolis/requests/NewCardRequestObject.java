package com.codurance.retropolis.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewCardRequestObject {

  @NotNull(message = "Text cannot be empty")
  @Size(min = 1, message = "Text must not be less than 1 character")
  private String text;

  @NotNull(message = "Column id cannot be empty")
  private Long columnId;

  @NotNull(message = "Username cannot be null")
  private String username;

  public NewCardRequestObject() {
  }

  public NewCardRequestObject(String text, Long columnId, String username) {
    this.text = text;
    this.columnId = columnId;
    this.username = username;
  }

  public String getText() {
    return text;
  }

  public Long getColumnId() {
    return columnId;
  }

  public String getUsername() {
    return username;
  }
}
