package com.codurance.retropolis.exceptions;

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException() {
    super("Unauthorized");
  }
}
