package com.codurance.retropolis.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UpVoteRequestObject {

  @NotNull(message = "Email is required")
  @Email(message = "Email is invalid")
  private String email;

  public UpVoteRequestObject() {
  }

  public UpVoteRequestObject(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }
}
