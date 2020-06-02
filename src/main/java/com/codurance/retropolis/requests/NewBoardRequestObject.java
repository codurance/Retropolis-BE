package com.codurance.retropolis.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewBoardRequestObject {

  @NotNull(message = "Title cannot be empty")
  @Size(min = 1, message = "Title must not be less than 1 character")
  private String title;
  private String userEmail;

  public NewBoardRequestObject() {
  }

  public NewBoardRequestObject(String title, String userEmail) {
    this.title = title;
    this.userEmail = userEmail;
  }

  public String getTitle() {
    return title;
  }

  public String getUserEmail() {
    return userEmail;
  }

}
