package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.User;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresUserRepository implements UserRepository {

  @Override
  public User findByEmail(String email) {
    throw new UnsupportedOperationException();
  }

  @Override
  public User registerBy(String email) {
    throw new UnsupportedOperationException();
  }
}
