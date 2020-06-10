package com.codurance.retropolis.entities;

public class User {

  public final String username;
  public final String email;
  private Long id;

  public User(Long id, String email, String username) {
    this.id = id;
    this.email = email;
    this.username = username;
  }

  public User(String email, String username) {
    this.email = email;
    this.username = username;
  }

  public Long getId() {
    return id;
  }
}
