package com.codurance.retropolis.requests;

public class UpdateCardRequestObject {

  private String newText;

  public UpdateCardRequestObject() {
  }

  public UpdateCardRequestObject(String newText) {
    this.newText = newText;
  }

  public String getNewText() {
    return newText;
  }
}
