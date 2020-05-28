package com.codurance.retropolis.requests;

public class UpVoteRequestObject {

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
