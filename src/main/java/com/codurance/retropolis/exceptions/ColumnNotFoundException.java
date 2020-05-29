package com.codurance.retropolis.exceptions;

public class ColumnNotFoundException extends RuntimeException {

  public ColumnNotFoundException() {
    super("Column Id is not valid");
  }
}
