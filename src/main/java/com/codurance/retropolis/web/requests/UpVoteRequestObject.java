package com.codurance.retropolis.web.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UpVoteRequestObject {

  @NotNull(message = "Email is required")
  @Email(message = "Email is invalid")
  private String email;

  @NotNull(message = "AddVote parameter is required")
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
