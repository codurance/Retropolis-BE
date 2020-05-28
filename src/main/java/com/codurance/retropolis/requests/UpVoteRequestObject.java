package com.codurance.retropolis.requests;

import javax.validation.constraints.NotNull;

public class UpVoteRequestObject {

  @NotNull(message = "Username cannot be empty")
  private String username;
  @NotNull(message = "addVote cannot be empty")
  private final Boolean addVote;

  public UpVoteRequestObject(String username, Boolean addVote) {
    this.username = username;
    this.addVote = addVote;
  }

  public String getUsername() {
    return username;
  }

  public boolean isAddVote() {
    return addVote;
  }
}
