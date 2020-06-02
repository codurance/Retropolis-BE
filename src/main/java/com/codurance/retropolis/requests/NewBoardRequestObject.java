package com.codurance.retropolis.requests;

public class NewBoardRequestObject {

  private final String boardName;
  private final String userEmail;

  public NewBoardRequestObject(String boardName, String userEmail) {
    this.boardName = boardName;
    this.userEmail = userEmail;
  }
}
