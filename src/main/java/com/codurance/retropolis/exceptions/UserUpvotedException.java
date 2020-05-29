package com.codurance.retropolis.exceptions;

public class UserUpvotedException extends RuntimeException {

  public UserUpvotedException() {
    super("User already up-voted!");
  }

}
