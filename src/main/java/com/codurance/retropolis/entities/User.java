package com.codurance.retropolis.entities;

public class User {
  public final Long id;
  public final String email;

  public User(Long id, String email) {
    this.id = id;
    this.email = email;
  }

  public long getId() {
    return id;
  }
}
