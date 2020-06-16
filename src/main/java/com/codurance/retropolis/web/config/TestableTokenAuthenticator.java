package com.codurance.retropolis.web.config;

import com.codurance.retropolis.config.Environment;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(Environment.TEST)
public class TestableTokenAuthenticator implements TokenAuthenticator {

  @Override
  public String getEmail(String token) {
    return "john.doe@codurance.com";
  }

  @Override
  public String getUsername(String token) {
    return "John Doe";
  }
}
