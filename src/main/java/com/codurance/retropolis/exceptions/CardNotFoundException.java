package com.codurance.retropolis.exceptions;

public class CardNotFoundException extends RuntimeException {

  public CardNotFoundException() {
    super("Card Id is not valid");
  }

}
