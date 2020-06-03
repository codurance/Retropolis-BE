package com.codurance.retropolis.repositories;

import com.codurance.retropolis.entities.User;

public interface UserRepository {

  User findByEmail(String email);

  User register(String email);

  void addToBoard(Long userId, Long boardId);
}
