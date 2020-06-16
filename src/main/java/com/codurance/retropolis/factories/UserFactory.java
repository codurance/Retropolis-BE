package com.codurance.retropolis.factories;

import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.web.config.TokenAuthenticator;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

  private final TokenAuthenticator tokenAuthenticator;

  @Autowired
  public UserFactory(
      TokenAuthenticator tokenAuthenticator) {
    this.tokenAuthenticator = tokenAuthenticator;
  }

  public User create(String token) throws GeneralSecurityException, IOException {
    return new User(tokenAuthenticator.getEmail(token), tokenAuthenticator.getUsername(token));
  }

}
