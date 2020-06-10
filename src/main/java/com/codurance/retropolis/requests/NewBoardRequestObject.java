package com.codurance.retropolis.requests;

import com.codurance.retropolis.entities.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewBoardRequestObject {

  @NotNull(message = "Title cannot be empty")
  @Size(min = 1, message = "Title must not be less than 1 character")
  private String title;

  @NotNull(message = "Email is required")
  @Email(message = "Email is invalid")
  private String userEmail;

  private User user;

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

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }
}
