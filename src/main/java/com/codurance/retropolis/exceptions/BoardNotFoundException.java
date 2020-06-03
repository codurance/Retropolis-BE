package com.codurance.retropolis.exceptions;

public class BoardNotFoundException extends RuntimeException {

  public BoardNotFoundException() {
    super("Board Id is not valid");
  }

}
