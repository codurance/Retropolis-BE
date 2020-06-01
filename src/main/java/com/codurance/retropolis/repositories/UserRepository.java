package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.User;

public interface UserRepository {

  User findByEmail(String email);

  User register(String email);
}
