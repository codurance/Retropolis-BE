package com.codurance.retropolis.exceptions;

public class ColumnNotFoundException extends RuntimeException {

  public ColumnNotFoundException(String message) {
    super(message);
  }
}
