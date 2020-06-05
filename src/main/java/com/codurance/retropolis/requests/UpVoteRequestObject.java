package com.codurance.retropolis.requests;

import javax.validation.constraints.NotNull;

public class UpVoteRequestObject {

  @NotNull(message = "Username cannot be empty")
  private String username;

  public UpVoteRequestObject() {
  }

  public UpVoteRequestObject(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
