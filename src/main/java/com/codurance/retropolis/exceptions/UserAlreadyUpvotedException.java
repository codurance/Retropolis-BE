package com.codurance.retropolis.exceptions;

public class UserAlreadyUpvotedException extends RuntimeException {

  public UserAlreadyUpvotedException() {
    super("User already up-voted!");
  }

}
