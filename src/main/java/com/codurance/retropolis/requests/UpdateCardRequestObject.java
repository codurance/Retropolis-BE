package com.codurance.retropolis.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateCardRequestObject {

  @NotNull(message = "Text cannot be empty")
  @Size(min = 1, message = "Text must not be less than 1 character")
  private String newText;

  public UpdateCardRequestObject() {
  }

  public UpdateCardRequestObject(String newText) {
    this.newText = newText;
  }

  public String getNewText() {
    return newText;
  }
}
