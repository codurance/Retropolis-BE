package com.codurance.retropolis.repositories;

import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.UserNotFoundException;
import com.codurance.retropolis.repositories.mappers.UserMapper;
import java.sql.PreparedStatement;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresUserRepository implements UserRepository {

  private final String SELECT_USER_BY_EMAIL = "select * from users where email = ?";
  private final String SELECT_USER_BY_ID = "select * from users where id = ?";
  private final String INSERT_USER = "insert into users (email) values (?)";

  private final JdbcTemplate jdbcTemplate;

  public PostgresUserRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public User findByEmail(String email) {
    try {
      return jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL, new UserMapper(), email);
    } catch (RuntimeException exception) {
      throw new UserNotFoundException();
    }
  }

  @Override
  public User register(String email) {
    KeyHolder key = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement statement = connection.prepareStatement(INSERT_USER, new String[]{"id"});
      statement.setString(1, email);
      return statement;
    }, key);

    Long id = Objects.requireNonNull(key.getKey()).longValue();
    return findById(id);
  }

  private User findById(Long id) {
    try {
      return jdbcTemplate.queryForObject(SELECT_USER_BY_ID, new UserMapper(), id);
    } catch (RuntimeException exception) {
      throw new UserNotFoundException();
    }
  }

}