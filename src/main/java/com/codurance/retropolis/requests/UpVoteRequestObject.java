package com.codurance.retropolis.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UpVoteRequestObject {

  @NotNull(message = "Email is required")
  @Email(message = "Email is invalid")
  private String email;
  private Boolean addVote;

  public UpVoteRequestObject() {
  }

  public UpVoteRequestObject(String email, Boolean addVote) {
    this.email = email;
    this.addVote = addVote;
  }

  public String getEmail() {
    return email;
  }

  public Boolean getAddVote() {
    return addVote;
  }
}
