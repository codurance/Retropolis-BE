package com.codurance.retropolis.factories;

import com.codurance.retropolis.config.web.GoogleTokenAuthenticator;
import com.codurance.retropolis.entities.User;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

  private final GoogleTokenAuthenticator tokenAuthenticator;

  public UserFactory(
      GoogleTokenAuthenticator tokenAuthenticator) {
    this.tokenAuthenticator = tokenAuthenticator;
  }

  public User create(String token) throws GeneralSecurityException, IOException {
    return new User(tokenAuthenticator.getEmail(token), tokenAuthenticator.getUserName(token));
  }
}
