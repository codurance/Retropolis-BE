package com.codurance.retropolis.web.responses;

public class UserBoardResponseObject {

  private Long id;
  private String title;

  public UserBoardResponseObject(Long id, String title) {
    this.id = id;
    this.title = title;
  }

  public UserBoardResponseObject() {
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

}
