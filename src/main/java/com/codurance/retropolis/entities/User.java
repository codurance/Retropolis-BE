package com.codurance.retropolis.entities;

public class User {
  public final Long id;
  public final String email;
  private String username;

  public User(Long id, String email, String username) {
    this.id = id;
    this.email = email;
    this.username = username;
  }
}
