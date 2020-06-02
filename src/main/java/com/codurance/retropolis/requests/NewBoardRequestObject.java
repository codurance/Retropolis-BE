package com.codurance.retropolis.requests;

public class NewBoardRequestObject {

  private String title;
  private String userEmail;

  public NewBoardRequestObject() {
  }

  public NewBoardRequestObject(String title, String userEmail) {
    this.title = title;
    this.userEmail = userEmail;
  }

  public String getTitle() {
    return title;
  }

  public String getUserEmail() {
    return userEmail;
  }

}
