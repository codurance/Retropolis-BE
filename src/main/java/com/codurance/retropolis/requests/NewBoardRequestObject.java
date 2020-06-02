package com.codurance.retropolis.requests;

public class NewBoardRequestObject {

  private String boardName;
  private String userEmail;

  public NewBoardRequestObject() {
  }

  public NewBoardRequestObject(String boardName, String userEmail) {
    this.boardName = boardName;
    this.userEmail = userEmail;
  }

  public String getBoardName() {
    return boardName;
  }

  public String getUserEmail() {
    return userEmail;
  }

}
