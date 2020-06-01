package com.codurance.retropolis.requests;

public class NewBoardRequestObject {

  private final String title;
  private final String email;

  public NewBoardRequestObject(String title, String email) {
    this.title = title;
    this.email = email;
  }
}
