package com.codurance.retropolis.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewCardRequestObject {

  @NotNull(message = "Text cannot be empty")
  @Size(min = 1, message = "Text must not be less than 1 character")
  private String text;

  @NotNull(message = "Column id cannot be empty")
  private Long columnId;

  @NotNull(message = "Email is required")
  @Email(message = "Email is invalid")
  private String email;

  private Long userId;

  public NewCardRequestObject() {
  }

  public NewCardRequestObject(String text, Long columnId, String email) {
    this.text = text;
    this.columnId = columnId;
    this.email = email;
  }

  public String getText() {
    return text;
  }

  public Long getColumnId() {
    return columnId;
  }

  public String getEmail() {
    return email;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
