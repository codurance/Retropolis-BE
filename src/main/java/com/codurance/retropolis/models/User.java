package com.codurance.retropolis.models;

public class User {

  private long userId;

  public User(long userId) {
    this.userId = userId;
  }

  public long getId() {
    return userId;
  }
}
