package com.codurance.retropolis.requests;

public class UpVoteRequestObject {

  private String username;
  private final boolean addVote;

  public UpVoteRequestObject(String username, boolean addVote) {
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
